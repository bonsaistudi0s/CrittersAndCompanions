package com.github.eterdelta.crittersandcompanions.entity.brain;

import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
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
