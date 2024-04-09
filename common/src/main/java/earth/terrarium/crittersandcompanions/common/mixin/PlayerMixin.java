package earth.terrarium.crittersandcompanions.common.mixin;

import earth.terrarium.crittersandcompanions.common.capability.Bubbleable;
import earth.terrarium.crittersandcompanions.common.capability.Grapplable;
import earth.terrarium.crittersandcompanions.common.entity.GrapplingHookEntity;
import net.minecraft.nbt.CompoundTag;
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
public abstract class PlayerMixin extends LivingEntity implements Bubbleable, Grapplable {
    @Unique
    private static final String BUBBLE_TAG = "HasBubble";
    @Unique
    private boolean crittersandcompanions$hasBubble = false;
    @Unique
    private GrapplingHookEntity crittersandcompanions$hook = null;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void crittersandcompanions$load(CompoundTag compoundTag, CallbackInfo ci) {
        crittersandcompanions$setActive(compoundTag.getBoolean(BUBBLE_TAG));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void crittersandcompanions$save(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean(BUBBLE_TAG, crittersandcompanions$isActive());
    }

    @Override
    public boolean crittersandcompanions$isActive() {
        return crittersandcompanions$hasBubble;
    }

    @Override
    public void crittersandcompanions$setActive(boolean active) {
        crittersandcompanions$hasBubble = active;
    }

    @Override
    public GrapplingHookEntity crittersandcompanions$getHook() {
        return crittersandcompanions$hook;
    }

    @Override
    public void crittersandcompanions$setHook(GrapplingHookEntity hook) {
        this.crittersandcompanions$hook = hook;
    }
}
