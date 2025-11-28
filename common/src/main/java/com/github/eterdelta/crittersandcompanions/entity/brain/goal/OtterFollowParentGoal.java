package com.github.eterdelta.crittersandcompanions.entity.brain.goal;

import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;

public class OtterFollowParentGoal extends FollowParentGoal {
    private final OtterEntity otter;

    public OtterFollowParentGoal(OtterEntity otterEntity) {
        super(otterEntity, 1.2D);
        this.otter = otterEntity;
    }

    @Override
    public boolean canUse() {
        return !this.otter.isEating() && super.canUse();
    }
}
