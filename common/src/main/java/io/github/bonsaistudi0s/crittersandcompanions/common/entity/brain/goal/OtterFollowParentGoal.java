package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.world.entity.ai.goal.FollowParentGoal;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.OtterEntity;

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
