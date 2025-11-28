package com.github.eterdelta.crittersandcompanions.entity.brain.goal;

import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.world.entity.ai.goal.BreedGoal;

public class OtterBreedGoal extends BreedGoal {
    private final OtterEntity otter;

    public OtterBreedGoal(OtterEntity otterEntity) {
        super(otterEntity, 1.0D);
        this.otter = otterEntity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.otter.isEating();
    }
}
