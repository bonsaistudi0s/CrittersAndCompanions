package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;

public class SprintingFollowOwnerGoal extends FollowOwnerGoal {

    private final TamableAnimal mob;
    private final double speedModifier;
    private final float sprintAtSqr;
    private int timeToRecalcPath;

    public SprintingFollowOwnerGoal(TamableAnimal mob, double speedModifier, float start, float sprintAt, float stop) {
        super(mob, 1.0D, start, stop);
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.sprintAtSqr = sprintAt * sprintAt;
    }

    @Override
    public void tick() {
        boolean shouldTeleport = mob.shouldTryTeleportToOwner();
        var owner = mob.getOwner();
        if (owner == null) return;

        if (!shouldTeleport) {
            mob.getLookControl().setLookAt(owner, 10.0F, (float) mob.getMaxHeadXRot());
        }

        if (--timeToRecalcPath <= 0) {
            timeToRecalcPath = adjustedTickDelay(10);
            if (shouldTeleport) {
                mob.tryToTeleportToOwner();
            } else {
                var distance = mob.distanceToSqr(owner);
                var speed = distance >= sprintAtSqr ? speedModifier : 1.0;
                mob.getNavigation().moveTo(owner, speed);
            }

        }
    }

}
