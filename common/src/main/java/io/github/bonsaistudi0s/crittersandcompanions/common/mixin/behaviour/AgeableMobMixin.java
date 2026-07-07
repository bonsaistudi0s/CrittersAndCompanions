package io.github.bonsaistudi0s.crittersandcompanions.common.mixin.behaviour;

import net.minecraft.world.entity.AgeableMob;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BehaviourDriven;

@Mixin(AgeableMob.class)
public class AgeableMobMixin implements BehaviourDriven {

    @Inject(
            method = "setAge",
            at = @At("TAIL")
    )
    public void cac$behaviourSetAge(int age, CallbackInfo ci) {
        getBehaviours().forEach(it -> it.setAge(age));
    }
}
