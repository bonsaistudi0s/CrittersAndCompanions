package com.github.eterdelta.crittersandcompanions.entity.brain.behaviour;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;

public interface Behaviour {

    default void aiStep() {
    }

    default void save(CompoundTag nbt) {
    }

    default void read(CompoundTag nbt) {
    }

    default void defineSyncedData(SynchedEntityData.Builder builder) {

    }

    default void finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData groupData) {

    }

}
