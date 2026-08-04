package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.animation.BugAnimations;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.DancingStrollGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.TameablePanicGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.mixin.WallClimberNavigationAccessor;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
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


public class SnailEntity extends TamableAnimal implements GeoEntity {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SnailEntity.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(SnailEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> WAKING_UP_TICKS = SynchedEntityData.defineId(SnailEntity.class,
            EntityDataSerializers.INT);

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.SNAIL.getKey());

    private static final ResourceLocation SHELL_KNOCKBACK_RESIST = CrittersAndCompanions.createId("shell_knockback_resistance");
    private static final ResourceLocation SHELL_ARMOR = CrittersAndCompanions.createId("shell_armor");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SnailEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        EntityUtils.applyAwarenessMaluses(this);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new VariantBehaviour(this, VARIANT, 3));
        behaviours.add(new DancingBehaviour(this));
        behaviours.add(new TameableBehaviour(this, TAGS));
        behaviours.add(new ClimbingBehaviour(this, CLIMBING));
        behaviours.add(new HealthRegenerationBehaviour(this));
        behaviours.add(new SlimeHarvestBehaviour());
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
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F) {
            @Override
            public boolean canUse() {
                return !isOrderedToSit() && super.canUse();
            }
        });
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WAKING_UP_TICKS, -1);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        var handStack = player.getItemInHand(hand);
        if (handStack.is(Items.GLASS_BOTTLE)) {
            var harvest = behaviour(SlimeHarvestBehaviour.class);
            if (!level().isClientSide()) {
                if (harvest.isReady()) {
                    handStack.consume(1, player);
                    var slimeBottle = new ItemStack(CACItems.SNAIL_SLIME_BOTTLE.get());
                    if (handStack.isEmpty()) {
                        player.setItemInHand(hand, slimeBottle);
                    } else if (!player.getInventory().add(slimeBottle)) {
                        player.drop(slimeBottle, false);
                    }
                    level().playSound(null, getX(), getY(), getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.8F);
                    harvest.startCooldown();
                }
                return InteractionResult.sidedSuccess(false);
            }

            return InteractionResult.sidedSuccess(true);
        }

        return super.mobInteract(player, hand);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(TAGS.food());
    }

    @Override
    public @Nullable SnailEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        var baby = CACEntities.SNAIL.get().create(level);
        if (baby == null) {
            return null;
        }

        if (otherParent instanceof SnailEntity otherSnailParent) {
            baby.behaviour(VariantBehaviour.class).inherit(this, otherSnailParent);
        }

        if (isTame()) {
            baby.setOwnerUUID(getOwnerUUID());
            baby.setTame(true, true);
        }

        return baby;
    }

    @Override
    public boolean onClimbable() {
        return behaviour(ClimbingBehaviour.class).isClimbing();
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        super.travel(travelVector);

        if (onClimbable()) {
            Vec3 movement = getDeltaMovement();
            double climbSpeed = getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.5f;
            if (movement.y > climbSpeed) {
                setDeltaMovement(movement.x, climbSpeed, movement.z);
            }
        }
    }

    @Override
    public void jumpFromGround() {
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new SnailNavigation(this, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(SnailAnimations.createController(this));
    }

    @Override
    public void setOrderedToSit(boolean orderedToSit) {
        if (level().isClientSide()) {
            super.setOrderedToSit(orderedToSit);
            return;
        }

        if (isWakingUp()) {
            return;
        }

        if (orderedToSit) {
            triggerAnim("controller", "hide");
        } else {
            setWakingUpTicks(26);
            triggerAnim("controller", "wake_up");
        }

        super.setOrderedToSit(orderedToSit);
        setInSittingPose(orderedToSit);

        updateShellDefenses(orderedToSit);
    }

    @Override
    protected boolean isImmobile() {
        return isWakingUp() || isOrderedToSit() || super.isImmobile();
    }

    private void updateShellDefenses(boolean isHiding) {
        var knockbackResistanceAttribute = this.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        var armorAttribute = this.getAttribute(Attributes.ARMOR);

        if (knockbackResistanceAttribute != null && armorAttribute != null) {
            if (isHiding) {
                if (!knockbackResistanceAttribute.hasModifier(SHELL_KNOCKBACK_RESIST)) {
                    knockbackResistanceAttribute.addTransientModifier(new AttributeModifier(SHELL_KNOCKBACK_RESIST, 0.4, AttributeModifier.Operation.ADD_VALUE));
                }

                if (!armorAttribute.hasModifier(SHELL_ARMOR)) {
                    armorAttribute.addTransientModifier(new AttributeModifier(SHELL_ARMOR, 10.0, AttributeModifier.Operation.ADD_VALUE));
                }
            } else {
                knockbackResistanceAttribute.removeModifier(SHELL_KNOCKBACK_RESIST);
                armorAttribute.removeModifier(SHELL_ARMOR);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) {
            return;
        }

        if (isWakingUp()) {
            setWakingUpTicks(getWakingUpTicks() - 1);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public @NotNull Vec3 getPassengerRidingPosition(@NotNull Entity passenger) {
        if (onClimbable()) {
            var wallFace = getClimbingWallFace();
            if (wallFace != null) {
                var away = wallFace.getOpposite();
                double awayOffset = 6.0 / 16.0;
                double heightOffset = -2.5 / 16.0;
                return position().add(away.getStepX() * awayOffset, heightOffset, away.getStepZ() * awayOffset);
            }
        }

        double backOffset = 2.0 / 16.0;
        double yawRad = getYRot() * (Math.PI / 180.0);
        return position().add(
                Math.sin(yawRad) * backOffset,
                8.2 / 16.0,
                -Math.cos(yawRad) * backOffset
        );
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull Entity.MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (onClimbable()) {
            passenger.setYRot(getYRot());
            passenger.yRotO = getYRot();
            passenger.setXRot(0.0F);
            passenger.xRotO = 0.0F;
            if (passenger instanceof Mob mob) {
                mob.yBodyRot = getYRot();
                mob.yHeadRot = getYRot();
            }
        }
    }

    @Nullable
    public Direction getClimbingWallFace() {
        var box = getBoundingBox();
        var level = level();
        for (var dir : Direction.Plane.HORIZONTAL) {
            var neighbourPos = BlockPos.containing(
                    box.getCenter().x + dir.getStepX() * (box.getXsize() / 2.0 + 0.1),
                    box.minY,
                    box.getCenter().z + dir.getStepZ() * (box.getZsize() / 2.0 + 0.1)
            );

            if (!level.isLoaded(neighbourPos)) {
                continue;
            }

            if (!level.getBlockState(neighbourPos).getCollisionShape(level, neighbourPos).isEmpty()) {
                return dir;
            }
        }
        return null;
    }

    public boolean isGaryVariant() {
        if (!hasCustomName()) {
            return false;
        }

        var customName = ChatFormatting.stripFormatting(getCustomName().getString());
        return customName.equalsIgnoreCase("gary");
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        if (isInSittingPose()) {
            return null;
        }

        if (isGaryVariant()) {
            return CACSounds.SNAIL_GARY_IDLE.get();
        }

        return super.getAmbientSound();
    }

    @Override
    public int getAmbientSoundInterval() {
        if (isGaryVariant()) {
            return 200;
        }

        return super.getAmbientSoundInterval();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        if (isGaryVariant()) {
            return CACSounds.SNAIL_GARY_HURT.get();
        }

        return SoundEvents.SLIME_HURT_SMALL;
    }

    @Override
    protected float getSoundVolume() {
        return 0.5F;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    public int getWakingUpTicks() {
        return this.entityData.get(WAKING_UP_TICKS);
    }

    public void setWakingUpTicks(int value) {
        this.entityData.set(WAKING_UP_TICKS, value);
    }

    private boolean isWakingUp() {
        return getWakingUpTicks() >= 0;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !isTame() && !hasCustomName();
    }

    private static class SnailNavigation extends WallClimberNavigation {

        public SnailNavigation(SnailEntity mob, Level level) {
            super(mob, level);
        }

        @Override
        public void stop() {
            super.stop();

            // the snail often continued to navigate to its last target position
            // because of a quirk in WallClimberNavigation
            ((WallClimberNavigationAccessor) this).setPathToPosition(null);
        }
    }

    private static class SnailAnimations extends BugAnimations<SnailEntity> {

        private static final RawAnimation SIT_ANIM = RawAnimation.begin().thenLoop("sit");
        private static final RawAnimation HIDE_ANIM = RawAnimation.begin().thenPlay("hide");
        private static final RawAnimation WAKE_UP_ANIM = RawAnimation.begin().thenPlay("wake_up");

        public SnailAnimations(Behaviours behaviours) {
            super(behaviours);
        }

        public static AnimationController<SnailEntity> createController(SnailEntity animatable) {
            return new AnimationController<>(animatable, "controller", 4, new SnailAnimations(animatable.getBehaviours()))
                    .triggerableAnim("wake_up", WAKE_UP_ANIM)
                    .triggerableAnim("hide", HIDE_ANIM);
        }

        @Override
        protected @Nullable RawAnimation getCustomAnimation(AnimationState<SnailEntity> state) {
            if (state.getAnimatable().isWakingUp()) {
                return null;
            }

            if (state.getAnimatable().isInSittingPose()) {
                return SIT_ANIM;
            }

            return null;
        }
    }
}
