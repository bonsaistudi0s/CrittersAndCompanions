package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.extension.ISilkLeashState;
import com.github.eterdelta.crittersandcompanions.item.PearlNecklaceItem;
import com.github.eterdelta.crittersandcompanions.item.SilkLeashItem;
import com.github.eterdelta.crittersandcompanions.network.CACPacketHandler;
import com.github.eterdelta.crittersandcompanions.network.ClientboundSilkLeashStatePacket;
import com.github.eterdelta.crittersandcompanions.registry.CACBlocks;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ISilkLeashState {

    @Unique
    private final Pair<Set<UUID>, Set<UUID>> savedLeashState = new ObjectObjectImmutablePair<>(new HashSet<>(), new HashSet<>());

    @Unique
    private boolean needsLeashStateLoad;

    @Unique
    private int leashStateLoadDelay;

    @Unique
    private final Set<LivingEntity> leashingEntities = new HashSet<>();

    @Unique
    private final Set<LivingEntity> leashedByEntities = new HashSet<>();

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void onAddAdditionalSaveData(CompoundTag compoundTag, CallbackInfo callbackInfo) {
        if (!this.level().isClientSide()) {
            ListTag leashingEntitiesList = new ListTag();
            for (Entity entity : getLeashingEntities()) {
                leashingEntitiesList.add(NbtUtils.createUUID(entity.getUUID()));
            }
            compoundTag.put("LeashingEntities", leashingEntitiesList);

            ListTag leashedByEntitiesList = new ListTag();
            for (Entity entity : getLeashedByEntities()) {
                leashedByEntitiesList.add(NbtUtils.createUUID(entity.getUUID()));
            }
            compoundTag.put("LeashedByEntities", leashedByEntitiesList);
        }
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void onReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo callbackInfo) {
        if (!this.level().isClientSide()) {
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
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void onTick(CallbackInfo callbackInfo) {
        if (!this.level().isClientSide()) {
            if (this.needsLeashStateLoad) {
                leashStateLoadDelay++;

                if (leashStateLoadDelay >= 20) {
                    Set<LivingEntity> leashingEntities = getLeashingEntities();
                    Set<LivingEntity> leashedByEntities = getLeashedByEntities();

                    for (UUID uuid : this.savedLeashState.first()) {
                        LivingEntity entity = (LivingEntity) ((ServerLevel) this.level()).getEntity(uuid);
                        if (entity != null) {
                            leashingEntities.add(entity);
                        }
                    }
                    this.savedLeashState.first().clear();

                    for (UUID uuid : this.savedLeashState.second()) {
                        LivingEntity entity = (LivingEntity) ((ServerLevel) this.level()).getEntity(uuid);
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

            int entitiesBefore = getLeashingEntities().size();

            getLeashingEntities().removeIf(leashedEntity -> {
                Vec3 distance = this.position().subtract(leashedEntity.position());
                double distanceSqr = distance.lengthSqr();

                if (distanceSqr > 14.0D) {
                    leashedEntity.setDeltaMovement(leashedEntity.getDeltaMovement().add(distance
                            .scale(0.1D)
                            .add(0.0D, 0.1D, 0.0D)));

                    if (distanceSqr > 28.0D) {
                        ISilkLeashState leashedLeashState = (ISilkLeashState) leashedEntity;
                        leashedLeashState.getLeashedByEntities().remove(this);
                        leashedLeashState.sendLeashState();
                        return true;
                    }

                }

                return false;
            });

            int unleashedEntities = entitiesBefore - getLeashingEntities().size();

            if (unleashedEntities > 0) {
                ItemEntity leadEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(CACItems.SILK_LEAD.get(), unleashedEntities));
                this.level().addFreshEntity(leadEntity);
                this.sendLeashState();
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;)V", ordinal = 0, shift = At.Shift.BY, by = 1), method = "die")
    private void onDie(DamageSource source, CallbackInfo callbackInfo) {
        int unleashedStates = 0;
        unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates((LivingEntity) ((Entity) this), null) - 1);
        unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates(null, (LivingEntity) ((Entity) this)) - 1);
        if (unleashedStates > 0) {
            ItemEntity leadEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(CACItems.SILK_LEAD.get(), unleashedStates));
            this.level().addFreshEntity(leadEntity);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.isInWater()Z"), method = "travel(Lnet/minecraft/world/phys/Vec3;)V")
    private boolean redirectIsInWater(LivingEntity entity) {
        return this.isInWater() || this.getFeetBlockState().is(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get());
    }

    @ModifyVariable(at = @At(value = "LOAD"), method = "aiStep()V", ordinal = 0)
    private boolean modifyWaterFlag(boolean flag) {
        return flag || this.getFeetBlockState().is(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get());
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
    public void sendLeashState() {
        CACPacketHandler.SILK_LEASH_STATE.sendToTracking(this,
                new ClientboundSilkLeashStatePacket(
                        new ClientboundSilkLeashStatePacket.LeashData(
                                this.getId(),
                                new IntArrayList(getLeashingEntities().stream().mapToInt(Entity::getId).toArray()),
                                new IntArrayList(getLeashedByEntities().stream().mapToInt(Entity::getId).toArray())
                        )
                ));
    }

    @Override
    public Set<LivingEntity> getLeashingEntities() {
        return this.leashingEntities;
    }

    @Override
    public Set<LivingEntity> getLeashedByEntities() {
        return this.leashedByEntities;
    }

}
