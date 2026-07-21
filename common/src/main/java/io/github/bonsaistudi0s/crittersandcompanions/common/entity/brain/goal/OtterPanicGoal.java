package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.OtterEntity;
import net.minecraft.world.entity.ai.goal.PanicGoal;

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
