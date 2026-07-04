package io.github.bonsaistudi0s.crittersandcompanions.common.extension;

import net.minecraft.world.entity.LivingEntity;

import java.util.Set;

public interface ISilkLeashState {

    void sendLeashState();

    Set<LivingEntity> getLeashingEntities();

    Set<LivingEntity> getLeashedByEntities();
}
