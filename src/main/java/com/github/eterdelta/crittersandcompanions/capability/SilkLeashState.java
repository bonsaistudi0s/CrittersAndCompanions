package com.github.eterdelta.crittersandcompanions.capability;

import net.minecraft.world.entity.LivingEntity;

import java.util.HashSet;
import java.util.Set;

public class SilkLeashState implements ISilkLeashStateCapability {
    private final Set<LivingEntity> leashingEntities = new HashSet<>();;
    private final Set<LivingEntity> leashedByEntities = new HashSet<>();;

    @Override
    public Set<LivingEntity> getLeashingEntities() {
        return this.leashingEntities;
    }

    @Override
    public Set<LivingEntity> getLeashedByEntities() {
        return this.leashedByEntities;
    }
}
