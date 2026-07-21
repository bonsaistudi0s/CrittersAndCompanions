package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.EnumSet;

public class AnimatedDelayedRangedAttackGoal<T extends Mob & GeoEntity & RangedAttackMob> extends Goal {

    private final T owner;
    private @Nullable LivingEntity target;
    private int attackTime = -1;
    private final double speedModifier;
    private int seeTime;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;

    private final String animationControllerName;
    private final String animationName;
    private int ticksUntilAttack;
    private final int windUpTicks;
    private boolean isWindingUp;

    public AnimatedDelayedRangedAttackGoal(final T owner, final double speedModifier, final int attackInterval, final float attackRadius, final String animationControllerName, final String animationName, final int windUpTicks) {
        this(owner, speedModifier, attackInterval, attackInterval, attackRadius, animationControllerName, animationName, windUpTicks);
    }

    public AnimatedDelayedRangedAttackGoal(final T owner, final double speedModifier, final int attackIntervalMin, final int attackIntervalMax, final float attackRadius, final String animationControllerName, final String animationName, final int windUpTicks) {
        this.owner = owner;
        this.speedModifier = speedModifier;
        this.attackIntervalMin = attackIntervalMin;
        this.attackIntervalMax = attackIntervalMax;
        this.attackRadius = attackRadius;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.animationControllerName = animationControllerName;
        this.animationName = animationName;
        this.windUpTicks = windUpTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        var bestTarget = this.owner.getTarget();
        if (bestTarget != null && bestTarget.isAlive()) {
            this.target = bestTarget;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || this.target.isAlive() && !this.owner.getNavigation().isDone();
    }

    @Override
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
        isWindingUp = false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        double targetDistSqr = this.owner.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        var hasLineOfSight = this.owner.getSensing().hasLineOfSight(this.target);
        if (hasLineOfSight) {
            this.seeTime++;
        } else {
            this.seeTime = 0;
        }

        this.owner.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (isWindingUp) {
            this.owner.getNavigation().stop();

            ticksUntilAttack--;

            if (ticksUntilAttack <= 0) {
                float dist = (float)Math.sqrt(targetDistSqr) / this.attackRadius;
                float power = Mth.clamp(dist, 0.1F, 1.0F);
                this.owner.performRangedAttack(this.target, power);
                this.attackTime = Mth.floor(dist * (this.attackIntervalMax - this.attackIntervalMin) + this.attackIntervalMin);
                isWindingUp = false;
            }

            return;
        }

        if (!(targetDistSqr > this.attackRadiusSqr) && this.seeTime >= 5) {
            this.owner.getNavigation().stop();
        } else {
            this.owner.getNavigation().moveTo(this.target, this.speedModifier);
        }

        if (--this.attackTime == 0) {
            if (!hasLineOfSight) {
                return;
            }

            isWindingUp = true;
            ticksUntilAttack = windUpTicks;

            owner.triggerAnim(animationControllerName, animationName);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(targetDistSqr) / this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
        }
    }
}
