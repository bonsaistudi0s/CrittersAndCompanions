package com.github.eterdelta.crittersandcompanions.entity.brain.behaviour;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;

public interface BehaviourDriven {

    Behaviours getBehaviours();

    default void aiStep() {
        getBehaviours().forEach(Behaviour::aiStep);
    }

    default void readAdditionalSaveData(CompoundTag nbt) {
        getBehaviours().forEach(it -> it.read(nbt));
    }

    default void addAdditionalSaveData(CompoundTag nbt) {
        getBehaviours().forEach(it -> it.save(nbt));
    }

    default SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData groupData) {
        getBehaviours().forEach(it -> it.finalizeSpawn(level, difficulty, spawnType, groupData));
        return groupData;
    }

    default  <T extends Behaviour> T behaviour(Class<T> type) {
        return getBehaviours().the(type);
    }

}
