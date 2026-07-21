package io.github.bonsaistudi0s.crittersandcompanions.common.mixin.behaviour;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BehaviourDriven;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin implements BehaviourDriven {

    @Inject(
            method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
            at = @At("HEAD")
    )
    private void saveBehaviourData(CompoundTag compound, CallbackInfo ci) {
        getBehaviours().forEach(it -> it.save(compound));
    }

    @Inject(
            method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
            at = @At("HEAD")
    )
    private void readBehaviourData(CompoundTag compound, CallbackInfo ci) {
        getBehaviours().forEach(it -> it.read(compound));
    }

    @Inject(
            method = "aiStep()V",
            at = @At("HEAD")
    )
    private void behaviourAiStep(CallbackInfo ci) {
        getBehaviours().forEach(Behaviour::aiStep);
    }

    @Inject(
            method = "tick()V",
            at = @At("HEAD")
    )
    private void behaviourServerTick(CallbackInfo ci) {
        var self = (Mob) (Object) this;
        if (self.level().isClientSide()) return;
        getBehaviours().forEach(Behaviour::serverTick);
    }

    @Inject(
            method = "defineSynchedData",
            at = @At("HEAD")
    )
    private void defineSynchedBehaviourData(CallbackInfo ci) {
        var self = (Mob) (Object) this;
        getBehaviours().forEach(it -> it.defineSyncedData(self.getEntityData()));
    }

    @Inject(
            method = "finalizeSpawn",
            at = @At("HEAD")
    )
    private void behaviourFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        getBehaviours().forEach(it -> it.finalizeSpawn(level, difficulty, reason, spawnData, dataTag));
    }

}
