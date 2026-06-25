package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.entity.animation.BugAnimations;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.Behaviours;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.DancingBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.HealthRegenerationBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.TameableBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.goal.DancingStrollGoal;
import com.github.eterdelta.crittersandcompanions.entity.brain.goal.TameablePanicGoal;
import com.github.eterdelta.crittersandcompanions.entity.projectiles.MudBallProjectile;
import com.github.eterdelta.crittersandcompanions.registry.AnimalTags;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WeevilEntity extends TamableAnimal implements GeoEntity, RangedAttackMob {

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.WEEVIL.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final DelayedRangedAttackGoal rangedAttackGoal = new DelayedRangedAttackGoal(this, 1, 20, 8, "controller", "throw", 5);
    private final HurtByTargetGoal hurtByTargetGoal = new HurtByTargetGoal(this);
    private final NearestAttackableTargetGoal<Mob> attackTargetGoal = new NearestAttackableTargetGoal<>(this, Mob.class, 5, true, false, this::shouldAttack);

    public WeevilEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        reassessTameGoals();
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new DancingBehaviour(this));
        behaviours.add(new TameableBehaviour(this, TAGS));
        behaviours.add(new HealthRegenerationBehaviour(this));
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new TameablePanicGoal(this, 1.25D));
        goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(4, TAGS.temptGoal(this));
        goalSelector.addGoal(5, new BreedGoal(this, 1.25D));
        goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.4D, 10F, 2F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        goalSelector.addGoal(8, new DancingStrollGoal<>(this, 1.0D));
    }

    @Override
    public void setTame(boolean tame, boolean applyTamingSideEffects) {
        super.setTame(tame, applyTamingSideEffects);
        reassessTameGoals();
    }

    private void reassessTameGoals() {
        goalSelector.removeGoal(rangedAttackGoal);
        targetSelector.removeGoal(this.hurtByTargetGoal);
        targetSelector.removeGoal(this.attackTargetGoal);

        if (isTame()) {
            goalSelector.addGoal(3, rangedAttackGoal);
            targetSelector.addGoal(1, this.hurtByTargetGoal);
            targetSelector.addGoal(2, this.attackTargetGoal);
        }
    }

    public boolean shouldAttack(LivingEntity entity) {
        return entity instanceof Enemy && !(entity instanceof Creeper);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(TAGS.food());
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob entity) {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                BugAnimations.createController(this)
                        .triggerableAnim("throw", RawAnimation.begin().thenPlay("throw"))
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return CACSounds.BUGS_HURT.get();
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float velocity) {
        var lookVec = getLookAngle().normalize().scale(0.5);
        var spawnPos = new Vec3(getX(), getY() + getBbHeight() / 2.0, getZ()).add(lookVec);

        var projectile = new MudBallProjectile(level(), this);
        projectile.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

        double x = target.getX() - projectile.getX();
        double targetCenterY = target.getY() + (target.getBbHeight() / 2.0);
        double y = targetCenterY - projectile.getY();
        double z = target.getZ() - projectile.getZ();
        double distance = Math.sqrt(x * x + z * z) * 0.2;
        projectile.shoot(x, y + distance, z, 1, 5);

        level().addFreshEntity(projectile);
        playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1, 0.4f / (getRandom().nextFloat() * 0.4f + 0.8f));
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        if (getOwner() != null) {
            var targetHasSameOwner = target instanceof TamableAnimal tamableAnimal && tamableAnimal.isOwnedBy(getOwner());
            if (targetHasSameOwner) {
                return false;
            }
        }

        return super.canAttack(target);
    }

    private static class DelayedRangedAttackGoal extends Goal {
        private final WeevilEntity owner;
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

        public DelayedRangedAttackGoal(final WeevilEntity owner, final double speedModifier, final int attackInterval, final float attackRadius, final String animationControllerName, final String animationName, final int windUpTicks) {
            this(owner, speedModifier, attackInterval, attackInterval, attackRadius, animationControllerName, animationName, windUpTicks);
        }

        public DelayedRangedAttackGoal(final WeevilEntity owner, final double speedModifier, final int attackIntervalMin, final int attackIntervalMax, final float attackRadius, final String animationControllerName, final String animationName, final int windUpTicks) {
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
}
