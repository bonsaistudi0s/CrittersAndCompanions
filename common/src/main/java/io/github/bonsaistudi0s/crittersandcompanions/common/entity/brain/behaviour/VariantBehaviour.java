package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;

public class VariantBehaviour implements Behaviour {
    private final Mob owner;
    private final EntityDataAccessor<Integer> dataAccessor;
    private final int count;

    public VariantBehaviour(Mob owner, EntityDataAccessor<Integer> dataAccessor, int count) {
        this.owner = owner;
        this.dataAccessor = dataAccessor;
        this.count = count;
    }

    public int getVariant() {
        return owner.getEntityData().get(dataAccessor);
    }

    public void setVariant(int variant) {
        owner.getEntityData().set(dataAccessor, Mth.clamp(variant, 0, count - 1));
    }

    @Override
    public void defineSyncedData(SynchedEntityData entityData) {
        entityData.define(dataAccessor, 0);
    }

    @Override
    public void finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag) {
        if (reason == MobSpawnType.BUCKET) return;
        setVariant(owner.getRandom().nextInt(0, count));
    }

    @Override
    public void read(CompoundTag nbt) {
        setVariant(nbt.getInt("Variant"));
    }

    @Override
    public void save(CompoundTag nbt) {
        nbt.putInt("Variant", getVariant());
    }

    public void inherit(Mob parentA, Mob parentB) {
        var inherited = owner.getRandom().nextBoolean()
                ? parentA
                : parentB;

        setVariant(inherited.behaviour(VariantBehaviour.class).getVariant());
    }

}
