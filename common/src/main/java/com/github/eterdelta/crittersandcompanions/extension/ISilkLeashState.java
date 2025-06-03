package com.github.eterdelta.crittersandcompanions.extension;

import java.util.Set;
import net.minecraft.world.entity.LivingEntity;

public interface ISilkLeashState {

    void sendLeashState();

    Set<LivingEntity> getLeashingEntities();

    Set<LivingEntity> getLeashedByEntities();
}
