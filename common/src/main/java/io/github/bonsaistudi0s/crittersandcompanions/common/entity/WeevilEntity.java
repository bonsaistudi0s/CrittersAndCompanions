package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.animation.BugAnimations;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.AnimatedDelayedRangedAttackGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.DancingStrollGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.TameablePanicGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.projectiles.MudBallProjectile;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACSounds;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WeevilEntity extends TamableAnimal implements GeoEntity, RangedAttackMob {

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.WEEVIL.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final AnimatedDelayedRangedAttackGoal<WeevilEntity> rangedAttackGoal = new AnimatedDelayedRangedAttackGoal<>(this, 1, 20, 8, "controller", "throw", 5);
    private final OwnerHurtByTargetGoal ownerHurtByTargetGoal = new OwnerHurtByTargetGoal(this);
    private final OwnerHurtTargetGoal ownerHurtTargetGoal = new OwnerHurtTargetGoal(this);
    private final HurtByTargetGoal hurtByTargetGoal = new HurtByTargetGoal(this);

    public WeevilEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        reassessTameGoals();
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
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
        goalSelector.addGoal(3, new BreedGoal(this, 1.25D));
        goalSelector.addGoal(4, TAGS.temptGoal(this));
        goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
        goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.4D, 10F, 2F));
        goalSelector.addGoal(7, new DancingStrollGoal<>(this, 1.0D));
        goalSelector.addGoal(8, TAGS.sittingTemptGoal(this));
        goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(10, new RandomLookAroundGoal(this));
    }

    @Override
    public void setTame(boolean tame, boolean applyTamingSideEffects) {
        super.setTame(tame, applyTamingSideEffects);
        reassessTameGoals();
    }

    private void reassessTameGoals() {
        goalSelector.removeGoal(rangedAttackGoal);
        targetSelector.removeGoal(this.ownerHurtByTargetGoal);
        targetSelector.removeGoal(this.ownerHurtTargetGoal);
        targetSelector.removeGoal(this.hurtByTargetGoal);

        if (isTame()) {
            goalSelector.addGoal(3, rangedAttackGoal);
            targetSelector.addGoal(1, this.ownerHurtByTargetGoal);
            targetSelector.addGoal(2, this.ownerHurtTargetGoal);
            targetSelector.addGoal(3, this.hurtByTargetGoal);
        }
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
    public WeevilEntity getBreedOffspring(ServerLevel level, AgeableMob entity) {
        var baby = CACEntities.WEEVIL.get().create(level);
        if (baby == null) {
            return null;
        }

        if (isTame()) {
            baby.setOwnerUUID(getOwnerUUID());
            baby.setTame(true, true);
        }

        return baby;
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

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (target instanceof Creeper || target instanceof Ghast || target instanceof ArmorStand) {
            return false;
        }

        return super.wantsToAttack(target, owner);
    }
}
