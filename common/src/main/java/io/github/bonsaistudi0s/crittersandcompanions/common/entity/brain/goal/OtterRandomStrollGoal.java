package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.OtterEntity;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

public class OtterRandomStrollGoal extends RandomStrollGoal {
    private final OtterEntity otter;

    public OtterRandomStrollGoal(OtterEntity otterEntity) {
        super(otterEntity, 1.0F, 20);
        this.otter = otterEntity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !(this.otter.isFloating() || this.otter.needsSurface() || this.otter.isEating());
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !(this.otter.isFloating() || this.otter.needsSurface() || this.otter.isEating());
    }
}
