package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.world.entity.ai.goal.PanicGoal;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.OtterEntity;

public class OtterPanicGoal extends PanicGoal {

    private final OtterEntity otter;

    public OtterPanicGoal(OtterEntity otter, double speedModifier) {
        super(otter, speedModifier);
        this.otter = otter;
    }

    @Override
    public void start() {
        super.start();
        otter.rejectFood();
    }
}
