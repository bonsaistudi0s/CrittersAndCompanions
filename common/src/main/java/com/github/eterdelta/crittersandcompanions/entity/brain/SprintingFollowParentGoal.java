package com.github.eterdelta.crittersandcompanions.entity.brain;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;

public class SprintingFollowParentGoal extends FollowOwnerGoal {

    private final TamableAnimal mob;
    private final double speedModifier;
    private final float sprintAtSqr;
    private int timeToRecalcPath;

    public SprintingFollowParentGoal(TamableAnimal mob, double speedModifier, float start, float sprintAt, float stop) {
        super(mob, 1.0D, start, stop, false);
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.sprintAtSqr = sprintAt * sprintAt;
    }

    @Override
    public void tick() {
        var owner = mob.getOwner();
        if (owner == null) return;

        var distance = mob.distanceToSqr(owner);

        if (distance >= sprintAtSqr && distance <= 144F && --timeToRecalcPath <= 0) {
            timeToRecalcPath = adjustedTickDelay(10);
            mob.getNavigation().moveTo(owner, speedModifier);
        } else {
            super.tick();
        }
    }

}
