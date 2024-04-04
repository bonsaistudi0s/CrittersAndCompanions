package earth.terrarium.crittersandcompanions.common.mixin;

import earth.terrarium.crittersandcompanions.common.capability.SilkLeashable;
import earth.terrarium.crittersandcompanions.common.item.SilkLeashItem;
import earth.terrarium.crittersandcompanions.common.network.NetworkHandler;
import earth.terrarium.crittersandcompanions.common.network.s2c.SilkLeashStatePacket;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import earth.terrarium.crittersandcompanions.common.item.PearlNecklaceItem;
import earth.terrarium.crittersandcompanions.common.registry.ModBlocks;
import net.minecraft.nbt.*;
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

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SilkLeashable {
    @Unique
    private final Set<LivingEntity> crittersandcompanions$leashingEntities = new HashSet<>();
    @Unique
    private final Set<LivingEntity> crittersandcompanions$leashedByEntities = new HashSet<>();

    @Unique
    private final Set<UUID> crittersandcompanions$leashingIds = new HashSet<>();
    @Unique
    private final Set<UUID> crittersandcompanions$leashedByIds = new HashSet<>();
    @Unique
    private int crittersandcompanions$leashState = 0;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void onAddAdditionalSaveData(CompoundTag compoundTag, CallbackInfo callbackInfo) {
        ListTag leashingEntitiesList = new ListTag();
        for (LivingEntity leashingEntity : crittersandcompanions$leashingEntities) {
            leashingEntitiesList.add(NbtUtils.createUUID(leashingEntity.getUUID()));
        }
        compoundTag.put("LeashingEntities", leashingEntitiesList);

        ListTag leashedByEntitiesList = new ListTag();
        for (LivingEntity leashedByEntity : crittersandcompanions$leashedByEntities) {
            leashedByEntitiesList.add(NbtUtils.createUUID(leashedByEntity.getUUID()));
        }
        compoundTag.put("LeashedByEntities", leashedByEntitiesList);
        sendLeashState();
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void onReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo callbackInfo) {
        ListTag entities = compoundTag.getList("LeashingEntities", Tag.TAG_INT_ARRAY);
        ListTag leashedByEntities = compoundTag.getList("LeashedByEntities", Tag.TAG_INT_ARRAY);

        for (Tag tag : entities) {
            UUID entityId = NbtUtils.loadUUID(tag);
            crittersandcompanions$leashingIds.add(entityId);
        }

        for (Tag tag : leashedByEntities) {
            UUID entityId = NbtUtils.loadUUID(tag);
            crittersandcompanions$leashedByIds.add(entityId);
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void onTick(CallbackInfo callbackInfo) {
        if (!this.level().isClientSide()) {
            if (!crittersandcompanions$leashingIds.isEmpty() || !crittersandcompanions$leashedByIds.isEmpty()) {
                crittersandcompanions$leashState++;

                if (crittersandcompanions$leashState > 20) {
                    crittersandcompanions$leashState = 0;
                    crittersandcompanions$leashingEntities.clear();
                    crittersandcompanions$leashedByEntities.clear();

                    ServerLevel serverLevel = (ServerLevel) this.level();
                    for (UUID entityId : crittersandcompanions$leashingIds) {
                        Entity entity = serverLevel.getEntity(entityId);
                        if (entity instanceof LivingEntity leashingEntity) {
                            crittersandcompanions$leashingEntities.add(leashingEntity);
                        }
                    }

                    for (UUID entityId : crittersandcompanions$leashedByIds) {
                        Entity entity = serverLevel.getEntity(entityId);
                        if (entity instanceof LivingEntity leashedByEntity) {
                            crittersandcompanions$leashedByEntities.add(leashedByEntity);
                        }
                    }

                    sendLeashState();
                    crittersandcompanions$leashingIds.clear();
                    crittersandcompanions$leashedByIds.clear();
                }
            }

            int unleashedEntities = 0;

            for (Iterator<LivingEntity> iterator = getLeashingEntities().iterator(); iterator.hasNext(); ) {
                LivingEntity leashedEntity = iterator.next();
                Vec3 distance = this.position().subtract(leashedEntity.position());
                double distanceSqr = distance.lengthSqr();

                if (distanceSqr > 14.0D) {
                    leashedEntity.setDeltaMovement(leashedEntity.getDeltaMovement().add(distance
                            .scale(0.1D)
                            .add(0.0D, 0.1D, 0.0D)));

                    if (distanceSqr > 28.0D) {
                        if (leashedEntity instanceof SilkLeashable leashedLeashState) {
                            leashedLeashState.getLeashedByEntities().remove(this);
                            leashedLeashState.sendLeashState();
                        }
                        iterator.remove();
                        unleashedEntities++;
                    }
                }
            }

            if (unleashedEntities > 0) {
                ItemEntity leadEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.SILK_LEAD.get(), unleashedEntities));
                this.level().addFreshEntity(leadEntity);
                sendLeashState();
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;)V", ordinal = 0, shift = At.Shift.BY, by = 1), method = "die")
    private void onDie(DamageSource source, CallbackInfo callbackInfo) {
        int unleashedStates = 0;
        unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates(level(), blockPosition(), (LivingEntity) ((Entity) this), null) - 1);
        unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates(level(), blockPosition(), null, (LivingEntity) ((Entity) this)) - 1);
        if (unleashedStates > 0) {
            ItemEntity leadEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.SILK_LEAD.get(), unleashedStates));
            this.level().addFreshEntity(leadEntity);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.isInWater()Z"), method = "travel(Lnet/minecraft/world/phys/Vec3;)V")
    private boolean redirectIsInWater(LivingEntity entity) {
        return this.isInWater() || this.getFeetBlockState().is(ModBlocks.SEA_BUNNY_SLIME_BLOCK.get());
    }

    @ModifyVariable(at = @At(value = "LOAD"), method = "aiStep()V", ordinal = 0)
    private boolean modifyWaterFlag(boolean flag) {
        return flag || this.getFeetBlockState().is(ModBlocks.SEA_BUNNY_SLIME_BLOCK.get());
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
    public Set<LivingEntity> getLeashingEntities() {
        return crittersandcompanions$leashingEntities;
    }

    @Override
    public Set<LivingEntity> getLeashedByEntities() {
        return crittersandcompanions$leashedByEntities;
    }

    @Override
    public void sendLeashState() {
        NetworkHandler.CHANNEL.sendToAllLoaded(
            new SilkLeashStatePacket(
                new SilkLeashStatePacket.LeashData(
                    this.getId(),
                    getLeashingEntities().stream().mapToInt(Entity::getId).boxed().toList(),
                    getLeashedByEntities().stream().mapToInt(Entity::getId).boxed().toList()
                )
            ), this.level(), this.blockPosition());
    }

}
