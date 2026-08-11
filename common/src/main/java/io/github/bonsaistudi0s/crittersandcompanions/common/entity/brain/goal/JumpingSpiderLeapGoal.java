package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class JumpingSpiderLeapGoal extends Goal {

    private final Mob mob;
    private LivingEntity target;
    private final float yd;
    private final float horizontalMultiplier;

    public JumpingSpiderLeapGoal(Mob mob, float yd, float horizontalMultiplier) {
        this.mob = mob;
        this.yd = yd;
        this.horizontalMultiplier = horizontalMultiplier;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    public boolean canUse() {
        if (this.mob.hasControllingPassenger()) {
            return false;
        } else {
            this.target = this.mob.getTarget();
            if (this.target == null) {
                return false;
            } else {
                var d = this.mob.distanceToSqr(this.target);
                if (!(d < 4.0D) && !(d > 16.0D)) {
                    if (!this.mob.onGround()) {
                        return false;
                    } else {
                        return this.mob.getRandom().nextInt(reducedTickDelay(5)) == 0;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    public boolean canContinueToUse() {
        return !this.mob.onGround();
    }

    public void start() {
        var deltaMovement = this.mob.getDeltaMovement();
        var targetDir = new Vec3(this.target.getX() - this.mob.getX(), 0.0D, this.target.getZ() - this.mob.getZ());
        if (targetDir.lengthSqr() > 1.0E-7D) {
            targetDir = targetDir.normalize().scale(this.horizontalMultiplier).add(deltaMovement.scale(0.2D));
        }

        this.mob.setDeltaMovement(targetDir.x, (double)this.yd, targetDir.z);
    }
}
