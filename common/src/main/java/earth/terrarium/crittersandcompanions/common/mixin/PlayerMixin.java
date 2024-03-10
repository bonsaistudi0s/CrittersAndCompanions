package earth.terrarium.crittersandcompanions.common.mixin;

import earth.terrarium.crittersandcompanions.common.capability.Bubbleable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements Bubbleable {
    @Unique
    private static final EntityDataAccessor<Boolean> HAS_BUBBLE = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    private static final String BUBBLE_TAG = "HasBubble";

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineCorrupted(CallbackInfo ci) {
        entityData.define(HAS_BUBBLE, false);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCorrupted(CompoundTag compoundTag, CallbackInfo ci) {
        setActive(compoundTag.getBoolean(BUBBLE_TAG));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveCorrupted(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean(BUBBLE_TAG, isActive());
    }

    @Override
    public boolean isActive() {
        return entityData.get(HAS_BUBBLE);
    }

    @Override
    public void setActive(boolean active) {
        entityData.set(HAS_BUBBLE, active);
    }
}
