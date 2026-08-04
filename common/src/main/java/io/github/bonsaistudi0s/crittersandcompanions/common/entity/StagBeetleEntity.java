package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.animation.BugAnimations;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.AnimatedDelayedMeleeAttackGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.DancingStrollGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.TameablePanicGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACSounds;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.EntityUtils;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.world.level.pathfinder.PathType;


public class StagBeetleEntity extends TamableAnimal implements GeoEntity {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(StagBeetleEntity.class, EntityDataSerializers.INT);
    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.STAG_BEETLE.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public StagBeetleEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        EntityUtils.applyAwarenessMaluses(this);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new VariantBehaviour(this, VARIANT, 6));
        behaviours.add(new DancingBehaviour(this));
        behaviours.add(new TameableBehaviour(this, TAGS));
        behaviours.add(new HealthRegenerationBehaviour(this));
        behaviours.add(new BabyHealthPenaltyBehaviour(this));
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new TameablePanicGoal(this, 1.25D));
        goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(3, new AnimatedDelayedMeleeAttackGoal<>(this, 1.0D, true, "controller", "hit", 4));
        goalSelector.addGoal(4, new BreedGoal(this, 1.25D));
        goalSelector.addGoal(5, TAGS.temptGoal(this));
        goalSelector.addGoal(6, new FollowParentGoal(this, 1.25D));
        goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.4D, 10F, 2F));
        goalSelector.addGoal(8, new DancingStrollGoal<>(this, 1.0D));
        goalSelector.addGoal(9, TAGS.sittingTemptGoal(this));
        goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(11, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (isBaby()) {
            super.setTarget(null);
            return;
        }

        super.setTarget(target);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.26D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D);
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (target instanceof Creeper || target instanceof Ghast || target instanceof ArmorStand) {
            return false;
        }

        if (target instanceof TamableAnimal tamable) {
            return !tamable.isTame() || tamable.getOwner() != owner;
        }

        return super.wantsToAttack(target, owner);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypeTags.IS_PROJECTILE) || super.isInvulnerableTo(source);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(TAGS.food());
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Override
    public @Nullable StagBeetleEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        var baby = CACEntities.STAG_BEETLE.get().create(level);
        if (baby == null) {
            return null;
        }

        if (otherParent instanceof StagBeetleEntity otherStagBeetleParent) {
            baby.behaviour(VariantBehaviour.class).inherit(this, otherStagBeetleParent);
        }

        if (isTame()) {
            baby.setOwnerUUID(getOwnerUUID());
            baby.setTame(true, true);
        }

        return baby;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(StagBeetleAnimations.createController(this));
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
    protected @NotNull AABB getAttackBoundingBox() {
        return super.getAttackBoundingBox().inflate(1.0D, 0.0D, 1.0D);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !isTame() && !hasCustomName();
    }

    private static class StagBeetleAnimations extends BugAnimations<StagBeetleEntity> {

        private static final RawAnimation HIT = RawAnimation.begin().thenPlay("hit");
        private static final RawAnimation DEATH = RawAnimation.begin().thenPlay("death");

        public StagBeetleAnimations(Behaviours behaviours) {
            super(behaviours);
        }

        public static AnimationController<StagBeetleEntity> createController(StagBeetleEntity animatable) {
            return new AnimationController<>(animatable, "controller", 4, new StagBeetleAnimations(animatable.getBehaviours()))
                    .triggerableAnim("hit", HIT);
        }

        @Override
        protected @Nullable RawAnimation getCustomAnimation(AnimationState<StagBeetleEntity> state) {
            if (state.getAnimatable().isDeadOrDying()) {
                return DEATH;
            }

            return null;
        }
    }
}
