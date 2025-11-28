package com.github.eterdelta.crittersandcompanions.entity.brain.goal;

import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

public class OtterRandomLookAroundGoal extends RandomLookAroundGoal {
    private final OtterEntity otter;

    public OtterRandomLookAroundGoal(OtterEntity otterEntity) {
        super(otterEntity);
        this.otter = otterEntity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.otter.isInWater() && !this.otter.isEating();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !this.otter.isInWater() && !this.otter.isEating();
    }
}
