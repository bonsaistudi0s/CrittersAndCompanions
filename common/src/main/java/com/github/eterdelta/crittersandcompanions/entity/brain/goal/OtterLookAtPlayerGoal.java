package com.github.eterdelta.crittersandcompanions.entity.brain.goal;

import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

public class OtterLookAtPlayerGoal extends LookAtPlayerGoal {
    private final OtterEntity otter;

    public OtterLookAtPlayerGoal(OtterEntity otterEntity) {
        super(otterEntity, Player.class, 8.0F);
        this.otter = otterEntity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !(this.otter.isInWater() || this.otter.isEating());
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !(this.otter.isInWater() || this.otter.isEating());
    }
}
