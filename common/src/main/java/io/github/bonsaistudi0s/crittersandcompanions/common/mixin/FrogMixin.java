package io.github.bonsaistudi0s.crittersandcompanions.common.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.DragonflyEntity;

@Mixin(Frog.class)
public class FrogMixin {

    @Inject(method = "canEat", at = @At("HEAD"), cancellable = true)
    private static void cac$injectFrogFoodConditions(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof DragonflyEntity dragonfly) {
            var hasArmor = !dragonfly.getArmor().isEmpty();
            if (hasArmor) {
                cir.setReturnValue(false);
            }
        }
    }
}
