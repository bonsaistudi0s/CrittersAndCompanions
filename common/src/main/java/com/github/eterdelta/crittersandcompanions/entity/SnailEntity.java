package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.entity.animation.BugAnimations;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.*;
import com.github.eterdelta.crittersandcompanions.entity.brain.goal.DancingStrollGoal;
import com.github.eterdelta.crittersandcompanions.entity.brain.goal.TameablePanicGoal;
import com.github.eterdelta.crittersandcompanions.registry.AnimalTags;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SnailEntity extends TamableAnimal implements GeoEntity {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SnailEntity.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(SnailEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.SNAIL.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SnailEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new VariantBehaviour(this, VARIANT, 3));
        behaviours.add(new DancingBehaviour(this));
        behaviours.add(new TameableBehaviour(this, TAGS));
        behaviours.add(new ClimbingBehaviour(this, CLIMBING));
        behaviours.add(new HealthRegenerationBehaviour(this));
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new TameablePanicGoal(this, 1.25D));
        goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(2, TAGS.temptGoal(this));
        goalSelector.addGoal(6, new BreedGoal(this, 1.25D));
        goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.4D, 10F, 2F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        goalSelector.addGoal(11, new DancingStrollGoal<>(this, 1.0D));
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
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob entity) {
        return CACEntities.SNAIL.get().create(level);
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
        return new WallClimberNavigation(this, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(BugAnimations.createController(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.SLIME_HURT_SMALL;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
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
            if (!level.getBlockState(neighbourPos).getCollisionShape(level, neighbourPos).isEmpty()) {
                return dir;
            }
        }
        return null;
    }
}
