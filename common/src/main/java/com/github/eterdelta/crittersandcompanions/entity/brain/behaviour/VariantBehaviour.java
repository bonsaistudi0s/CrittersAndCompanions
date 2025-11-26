package com.github.eterdelta.crittersandcompanions.entity.brain.behaviour;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;

public class VariantBehaviour implements Behaviour {

    private final Entity owner;
    private final EntityDataAccessor<Integer> dataAccessor;
    private final int count;

    public VariantBehaviour(Entity owner, EntityDataAccessor<Integer> dataAccessor, int count) {
        this.owner = owner;
        this.dataAccessor = dataAccessor;
        this.count = count;
    }

    public int getVariant() {
        return owner.getEntityData().get(dataAccessor);
    }

    public void setVariant(int variant) {
        owner.getEntityData().set(dataAccessor, Math.clamp(variant, 0, count - 1));
    }

    @Override
    public void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(dataAccessor, 0);
    }

    @Override
    public void finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData groupData) {
        setVariant(owner.getRandom().nextInt(0, count));
    }

    @Override
    public void read(CompoundTag nbt) {
        nbt.putInt("Variant", getVariant());
    }

    @Override
    public void save(CompoundTag nbt) {
        setVariant(nbt.getInt("Variant"));
    }

}
