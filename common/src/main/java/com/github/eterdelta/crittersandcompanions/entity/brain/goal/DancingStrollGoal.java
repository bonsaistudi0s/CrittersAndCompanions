package com.github.eterdelta.crittersandcompanions.entity.brain.goal;

import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.BehaviourDriven;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.DancingBehaviour;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class DancingStrollGoal<T extends PathfinderMob & BehaviourDriven> extends WaterAvoidingRandomStrollGoal {
    private final DancingBehaviour behaviour;

    public DancingStrollGoal(T entity, double speedModifier) {
        super(entity, speedModifier);
        this.behaviour = entity.getBehaviours().the(DancingBehaviour.class);
    }

    @Override
    public boolean canUse() {
        return !behaviour.isDancing() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !behaviour.isDancing() && super.canContinueToUse();
    }

}
