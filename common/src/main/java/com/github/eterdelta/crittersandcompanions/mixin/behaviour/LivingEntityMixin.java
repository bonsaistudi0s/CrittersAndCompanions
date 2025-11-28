package com.github.eterdelta.crittersandcompanions.mixin.behaviour;

import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.BehaviourDriven;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements BehaviourDriven {

    @Inject(
            method = "setRecordPlayingNearby(Lnet/minecraft/core/BlockPos;Z)V",
            at = @At("HEAD")
    )
    private void setRecordPlayingNearby(BlockPos pos, boolean active, CallbackInfo ci) {
        getBehaviours().forEach(it -> it.setRecordPlayingNearby(pos, active));
    }

    @Inject(
            method = "travel(Lnet/minecraft/world/phys/Vec3;)V",
            at = @At("HEAD")
    )
    private void behaviourTravel(Vec3 vec, CallbackInfo ci) {
        getBehaviours().forEach(it -> it.travel(vec));
    }

}
