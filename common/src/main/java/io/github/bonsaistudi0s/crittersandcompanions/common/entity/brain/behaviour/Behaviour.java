package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public interface Behaviour {

    default void aiStep() {
    }

    default void serverTick() {
    }

    default void save(CompoundTag nbt) {
    }

    default void read(CompoundTag nbt) {
    }

    default void defineSyncedData(SynchedEntityData entityData) {

    }

    default void finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag) {

    }

    default void setRecordPlayingNearby(BlockPos pos, boolean active) {

    }

    default InteractionResult mobInteract(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    default void dropEquipment() {
    }

    default void travel(Vec3 speed) {
    }

    default void setAge(int age) {
    }

}
