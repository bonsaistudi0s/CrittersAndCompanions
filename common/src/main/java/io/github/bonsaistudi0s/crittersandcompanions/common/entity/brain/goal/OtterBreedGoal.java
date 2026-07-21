package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.OtterEntity;
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
