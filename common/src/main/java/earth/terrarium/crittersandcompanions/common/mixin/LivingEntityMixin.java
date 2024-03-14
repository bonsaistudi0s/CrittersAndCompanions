package earth.terrarium.crittersandcompanions.common.mixin;

import earth.terrarium.crittersandcompanions.common.capability.SilkLeashable;
import earth.terrarium.crittersandcompanions.common.item.SilkLeashItem;
import earth.terrarium.crittersandcompanions.common.network.NetworkHandler;
import earth.terrarium.crittersandcompanions.common.network.s2c.SilkLeashStatePacket;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import earth.terrarium.crittersandcompanions.common.item.PearlNecklaceItem;
import earth.terrarium.crittersandcompanions.common.registry.ModBlocks;
import net.minecraft.nbt.*;
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
    private final Set<LivingEntity> leashingEntities = new HashSet<>();
    @Unique
    private final Set<LivingEntity> leashedByEntities = new HashSet<>();

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void onAddAdditionalSaveData(CompoundTag compoundTag, CallbackInfo callbackInfo) {
        IntArrayTag leashingEntitiesList = new IntArrayTag(leashingEntities.stream().map(Entity::getId).toList());
        compoundTag.put("LeashingEntities", leashingEntitiesList);

        IntArrayTag leashedByEntitiesList = new IntArrayTag(leashedByEntities.stream().map(Entity::getId).toList());
        compoundTag.put("LeashedByEntities", leashedByEntitiesList);
        sendLeashState();
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void onReadAdditionalSaveData(CompoundTag compoundTag, CallbackInfo callbackInfo) {
        int[] leashingEntitiesList = compoundTag.getIntArray("LeashingEntities");
        for (int id : leashingEntitiesList) {
            leashingEntities.add((LivingEntity) this.level().getEntity(id));
        }

        int[] leashedByEntitiesList = compoundTag.getIntArray("LeashedByEntities");
        for (int id : leashedByEntitiesList) {
            leashedByEntities.add((LivingEntity) this.level().getEntity(id));
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void onTick(CallbackInfo callbackInfo) {
        if (!this.level().isClientSide()) {
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
        unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates((LivingEntity) ((Entity) this), null) - 1);
        unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates(null, (LivingEntity) ((Entity) this)) - 1);
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
        return leashingEntities;
    }

    @Override
    public Set<LivingEntity> getLeashedByEntities() {
        return leashedByEntities;
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