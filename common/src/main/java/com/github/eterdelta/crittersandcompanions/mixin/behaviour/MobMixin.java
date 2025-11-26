package com.github.eterdelta.crittersandcompanions.mixin.behaviour;

import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.Behaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.BehaviourDriven;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
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
    private void saveBehaviourData(CompoundTag nbt, CallbackInfo ci) {
        getBehaviours().forEach(it -> it.save(nbt));
    }

    @Inject(
            method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
            at = @At("HEAD")
    )
    private void readBehaviourData(CompoundTag nbt, CallbackInfo ci) {
        getBehaviours().forEach(it -> it.read(nbt));
    }

    @Inject(
            method = "aiStep()V",
            at = @At("HEAD")
    )
    private void behaviourAiStep(CallbackInfo ci) {
        getBehaviours().forEach(Behaviour::aiStep);
    }

    @Inject(
            method = "defineSynchedData(Lnet/minecraft/network/syncher/SynchedEntityData$Builder;)V",
            at = @At("HEAD")
    )
    private void defineSyncedBehaviourData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        getBehaviours().forEach(it -> it.defineSyncedData(builder));
    }

    @Inject(
            method = "finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;",
            at = @At("HEAD")
    )
    private void defineSyncedBehaviourData(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData groupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        getBehaviours().forEach(it -> it.finalizeSpawn(level, difficulty, spawnType, groupData));
    }

}
