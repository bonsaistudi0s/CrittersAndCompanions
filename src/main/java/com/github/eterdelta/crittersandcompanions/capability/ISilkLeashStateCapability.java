package com.github.eterdelta.crittersandcompanions.capability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.Set;

@AutoRegisterCapability
public interface ISilkLeashStateCapability {

    Set<LivingEntity> getLeashingEntities();

    Set<LivingEntity> getLeashedByEntities();
}
