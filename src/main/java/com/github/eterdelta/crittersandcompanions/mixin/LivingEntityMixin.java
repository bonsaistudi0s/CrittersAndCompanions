package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.capability.CACCapabilities;
import com.github.eterdelta.crittersandcompanions.capability.ISilkLeashStateCapability;
import com.github.eterdelta.crittersandcompanions.entity.ILeashStateEntity;
import com.github.eterdelta.crittersandcompanions.item.PearlNecklaceItem;
import com.github.eterdelta.crittersandcompanions.item.SilkLeashItem;
import com.github.eterdelta.crittersandcompanions.network.CACPacketHandler;
import com.github.eterdelta.crittersandcompanions.network.ClientboundSilkLeashStatePacket;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILeashStateEntity {

    @Unique
    private final Pair<Set<UUID>, Set<UUID>> savedLeashState = new ObjectObjectImmutablePair<>(new HashSet<>(), new HashSet<>());

    @Unique
    private LazyOptional<ISilkLeashStateCapability> leashStateCache;

    @Unique
    private boolean needsLeashStateLoad;

    @Unique
    private int leashStateLoadDelay;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void onAddAdditionalSaveData(CompoundTag compoundTag, CallbackInfo callbackInfo) {
        if (!this.level.isClientSide()) {
            this.getLeashStateCache().ifPresent(state -> {
                ListTag leashingEntitiesList = new ListTag();
                for (Entity entity : state.getLeashingEntities()) {
                    leashingEntitiesList.add(NbtUtils.createUUID(entity.getUUID()));
                }
                compoundTag.put("LeashingEntities", leashingEntitiesList);

                ListTag leashedByEntitiesList = new ListTag();
                for (Entity entity : state.getLeashedByEntities()) {
                    leashedByEntitiesList.add(NbtUtils.createUUID(entity.getUUID()));
                }
                compoundTag.put("LeashedByEntities", leashedByEntitiesList);
            });
        }
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void onReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo callbackInfo) {
        if (!this.level.isClientSide()) {
            this.getLeashStateCache().ifPresent(state -> {
                ListTag leashingEntitiesList = compoundTag.getList("LeashingEntities", 11);
                for (Tag tag : leashingEntitiesList) {
                    UUID uuid = NbtUtils.loadUUID(tag);
                    this.savedLeashState.first().add(uuid);
                }

                ListTag leashedByEntitiesList = compoundTag.getList("LeashedByEntities", 11);
                for (Tag tag : leashedByEntitiesList) {
                    UUID uuid = NbtUtils.loadUUID(tag);
                    this.savedLeashState.second().add(uuid);
                }

                if (!leashingEntitiesList.isEmpty() || !leashedByEntitiesList.isEmpty()) {
                    this.needsLeashStateLoad = true;
                }
            });
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void onTick(CallbackInfo callbackInfo) {
        if (!this.level.isClientSide()) {
            this.getLeashStateCache().ifPresent(leashState -> {
                if (this.needsLeashStateLoad) {
                    leashStateLoadDelay++;

                    if (leashStateLoadDelay >= 20) {
                        Set<LivingEntity> leashingEntities = leashState.getLeashingEntities();
                        Set<LivingEntity> leashedByEntities = leashState.getLeashedByEntities();

                        for (UUID uuid : this.savedLeashState.first()) {
                            LivingEntity entity = (LivingEntity) ((ServerLevel) this.level).getEntity(uuid);
                            if (entity != null) {
                                leashingEntities.add(entity);
                            }
                        }
                        this.savedLeashState.first().clear();

                        for (UUID uuid : this.savedLeashState.second()) {
                            LivingEntity entity = (LivingEntity) ((ServerLevel) this.level).getEntity(uuid);
                            if (entity != null) {
                                leashedByEntities.add(entity);
                            }
                        }
                        this.savedLeashState.second().clear();

                        this.sendLeashState();
                        this.needsLeashStateLoad = false;
                        leashStateLoadDelay = 0;
                    }
                }

                int unleashedEntities = 0;

                for (Iterator<LivingEntity> iterator = leashState.getLeashingEntities().iterator(); iterator.hasNext(); ) {
                    LivingEntity leashedEntity = iterator.next();
                    Vec3 distance = this.position().subtract(leashedEntity.position());
                    double distanceSqr = distance.lengthSqr();

                    if (distanceSqr > 14.0D) {
                        leashedEntity.setDeltaMovement(leashedEntity.getDeltaMovement().add(distance
                                .scale(0.1D)
                                .add(0.0D, 0.1D, 0.0D)));

                        if (distanceSqr > 28.0D) {
                            ILeashStateEntity leashedStateEntity = (ILeashStateEntity) leashedEntity;
                            leashedStateEntity.getLeashStateCache().ifPresent(leashedLeashState -> {
                                leashedLeashState.getLeashedByEntities().remove(this);
                                leashedStateEntity.sendLeashState();
                            });
                            iterator.remove();
                            unleashedEntities++;
                        }
                    }
                }

                if (unleashedEntities > 0) {
                    ItemEntity leadEntity = new ItemEntity(this.getLevel(), this.getX(), this.getY(), this.getZ(), new ItemStack(CACItems.SILK_LEAD.get(), unleashedEntities));
                    this.getLevel().addFreshEntity(leadEntity);
                    this.sendLeashState();
                }
            });
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;)V", ordinal = 0, shift = At.Shift.BY, by = 1), method = "die")
    private void onDie(DamageSource source, CallbackInfo callbackInfo) {
        this.getLeashStateCache().ifPresent(state -> {
            int unleashedStates = 0;
            unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates((LivingEntity) ((Entity) this), null) - 1);
            unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates(null, (LivingEntity) ((Entity) this)) - 1);
            if (unleashedStates > 0) {
                ItemEntity leadEntity = new ItemEntity(this.getLevel(), this.getX(), this.getY(), this.getZ(), new ItemStack(CACItems.SILK_LEAD.get(), unleashedStates));
                this.getLevel().addFreshEntity(leadEntity);
            }
        });
    }

    @ModifyVariable(at = @At(value = "LOAD", ordinal = 3), method = "travel(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 1)
    private float modifySwimSpeed(float swimSpeed) {
        if (((Entity) this) instanceof Player player) {
            Inventory inventory = player.getInventory();

            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack stack = inventory.getItem(i);
                if (stack.getItem() instanceof PearlNecklaceItem pearlNecklaceItem) {
                    return swimSpeed + (swimSpeed * ((pearlNecklaceItem.getLevel() * 20) / 100.0F));
                }
            }
        }
        return swimSpeed;
    }

    @Override
    public LazyOptional<ISilkLeashStateCapability> getLeashStateCache() {
        if (this.leashStateCache == null) {
            this.leashStateCache = this.getCapability(CACCapabilities.SILK_LEASH_STATE);
            this.leashStateCache.addListener(self -> this.leashStateCache = null);
        }
        return this.leashStateCache;
    }

    @Override
    public void sendLeashState() {
        this.getLeashStateCache().ifPresent(leashState -> {
            CACPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this),
                    new ClientboundSilkLeashStatePacket(
                            new ClientboundSilkLeashStatePacket.LeashData(
                                    this.getId(),
                                    new IntArrayList(leashState.getLeashingEntities().stream().mapToInt(Entity::getId).toArray()),
                                    new IntArrayList(leashState.getLeashedByEntities().stream().mapToInt(Entity::getId).toArray())
                            )
                    ));
        });
    }
}
