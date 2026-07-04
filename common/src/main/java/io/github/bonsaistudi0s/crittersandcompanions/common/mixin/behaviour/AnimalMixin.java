package io.github.bonsaistudi0s.crittersandcompanions.common.mixin.behaviour;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BehaviourDriven;

@Mixin(Animal.class)
public class AnimalMixin implements BehaviourDriven {

    @Inject(
            method = "mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void behaviourMobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        for (var it : getBehaviours().all()) {
            var result = it.mobInteract(player, hand);
            if (result != InteractionResult.PASS) {
                cir.setReturnValue(result);
                break;
            }
        }
    }

}
