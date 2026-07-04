package io.github.bonsaistudi0s.crittersandcompanions.common.mixin.behaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BehaviourDriven;

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

    @Inject(
            method = "dropEquipment()V",
            at = @At("HEAD")
    )
    private void behaviourDropEquipment(CallbackInfo ci) {
        getBehaviours().forEach(Behaviour::dropEquipment);
    }

}
