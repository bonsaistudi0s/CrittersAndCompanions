package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.FerretEntity;

public class FerretSleepGoal extends Goal {
    private final FerretEntity mob;
    private final int countdownTime;
    private int countdown;

    public FerretSleepGoal(FerretEntity mob, int countdownTime) {
        this.mob = mob;
        this.countdownTime = countdownTime;
        this.countdown = mob.getRandom().nextInt(reducedTickDelay(countdownTime));
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    public boolean canUse() {
        if (mob.xxa == 0.0F && mob.yya == 0.0F && mob.zza == 0.0F) {
            return this.canSleep() || mob.isSleeping();
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        return this.canSleep();
    }

    private boolean canSleep() {
        if (this.countdown > 0) {
            --this.countdown;
            return false;
        } else {
            return mob.level().isNight();
        }
    }

    public void stop() {
        mob.setSleeping(false);
        this.countdown = mob.getRandom().nextInt(this.countdownTime);
    }

    public void start() {
        mob.setInSittingPose(false);
        mob.setJumping(false);
        mob.setSleeping(true);
        mob.getNavigation().stop();
        mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY(), mob.getZ(), 0.0D);
    }
}
