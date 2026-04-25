package com.github.eterdelta.crittersandcompanions.entity.brain.goal;

import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.ChestBehaviour;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class FreezeWhileChestAccessedGoal extends Goal {

    private final Mob mob;

    public FreezeWhileChestAccessedGoal(Mob mob) {
        this.mob = mob;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return mob.getBehaviours().the(ChestBehaviour.class).isBeingAccessed();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }
}
