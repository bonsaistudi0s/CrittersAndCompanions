package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

import software.bernie.geckolib.animatable.GeoEntity;

public class AnimatedDelayedMeleeAttackGoal<T extends PathfinderMob & GeoEntity> extends Goal {

    protected final T mob;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private long lastCanUseCheck;

    private final String animationControllerName;
    private final String animationName;
    private final int windUpTicks;
    private boolean isWindingUp;
    private int ticksUntilWindUpComplete;

    public AnimatedDelayedMeleeAttackGoal(T mob, double speedModifier, boolean followingTargetEvenIfNotSeen, String animationControllerName, String animationName, int windUpTicks) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.followingTargetEvenIfNotSeen = followingTargetEvenIfNotSeen;
        this.animationControllerName = animationControllerName;
        this.animationName = animationName;
        this.windUpTicks = windUpTicks;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        long time = this.mob.level().getGameTime();
        if (time - this.lastCanUseCheck < 20L) {
            return false;
        } else {
            this.lastCanUseCheck = time;
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                this.path = this.mob.getNavigation().createPath(target, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.mob.isWithinMeleeAttackRange(target);
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!this.followingTargetEvenIfNotSeen) {
            return !this.mob.getNavigation().isDone();
        } else if (!this.mob.isWithinRestriction(target.blockPosition())) {
            return false;
        } else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player)target).isCreative();
        }
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speedModifier);
        this.mob.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
        this.isWindingUp = false;
    }

    @Override
    public void stop() {
        LivingEntity target = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            this.mob.setTarget(null);
        }

        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
        this.isWindingUp = false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity != null) {
            this.mob.getLookControl().setLookAt(livingEntity, 30.0F, 30.0F);

            if (this.isWindingUp) {
                this.mob.getNavigation().stop();
                this.ticksUntilWindUpComplete--;

                if (this.ticksUntilWindUpComplete <= 0) {
                    if (this.mob.isWithinMeleeAttackRange(livingEntity) && this.mob.getSensing().hasLineOfSight(livingEntity)) {
                        this.mob.doHurtTarget(livingEntity);
                    }

                    this.resetAttackCooldown();
                    this.isWindingUp = false;
                }

                return;
            }

            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight(livingEntity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == (double)0.0F && this.pathedTargetY == (double)0.0F && this.pathedTargetZ == (double)0.0F || livingEntity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= (double)1.0F || this.mob.getRandom().nextFloat() < 0.05F)) {
                this.pathedTargetX = livingEntity.getX();
                this.pathedTargetY = livingEntity.getY();
                this.pathedTargetZ = livingEntity.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                double targetDistanceSqr = this.mob.distanceToSqr(livingEntity);
                if (targetDistanceSqr > (double)1024.0F) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (targetDistanceSqr > (double)256.0F) {
                    this.ticksUntilNextPathRecalculation += 5;
                }

                if (!this.mob.getNavigation().moveTo(livingEntity, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }

                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }

            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(livingEntity);
        }
    }

    protected void checkAndPerformAttack(LivingEntity livingEntity) {
        if (this.canPerformAttack(livingEntity)) {
            this.isWindingUp = true;
            this.ticksUntilWindUpComplete = this.windUpTicks;

            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.triggerAnim(this.animationControllerName, this.animationName);
        }
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20);
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean canPerformAttack(LivingEntity target) {
        return this.isTimeToAttack() && this.mob.isWithinMeleeAttackRange(target) && this.mob.getSensing().hasLineOfSight(target);
    }

    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected int getAttackInterval() {
        return this.adjustedTickDelay(20);
    }
}
