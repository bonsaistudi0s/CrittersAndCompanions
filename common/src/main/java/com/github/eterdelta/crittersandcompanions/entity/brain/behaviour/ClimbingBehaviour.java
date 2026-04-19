package com.github.eterdelta.crittersandcompanions.entity.brain.behaviour;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Mob;

public class ClimbingBehaviour implements Behaviour {
    private final Mob owner;
    private final EntityDataAccessor<Boolean> dataAccessor;

    public ClimbingBehaviour(Mob owner, EntityDataAccessor<Boolean> dataAccessor) {
        this.owner = owner;
        this.dataAccessor = dataAccessor;
    }

    public boolean isClimbing() {
        return owner.getEntityData().get(dataAccessor);
    }

    public void setClimbing(boolean climbing) {
        owner.getEntityData().set(dataAccessor, climbing);
    }

    @Override
    public void defineSyncedData(SynchedEntityData.Builder builder) {
        builder.define(dataAccessor, false);
    }

    @Override
    public void read(CompoundTag nbt) {
        setClimbing(nbt.getBoolean("Climbing"));
    }

    @Override
    public void save(CompoundTag nbt) {
        nbt.putBoolean("Climbing", isClimbing());
    }

    @Override
    public void serverTick() {
        setClimbing(owner.horizontalCollision);
    }
}
