package com.github.eterdelta.crittersandcompanions.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Random;

public class DumboOctopusEntity extends WaterAnimal implements IAnimatable {
    private static final EntityDataAccessor<Boolean> RESTING = SynchedEntityData.defineId(DumboOctopusEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(DumboOctopusEntity.class, EntityDataSerializers.INT);
    private final AnimationFactory factory = new AnimationFactory(this);
    public int restTimer;

    public DumboOctopusEntity(EntityType<? extends DumboOctopusEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new DumboOctopusMoveControl(90, 90, 1.0F, 1.0F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 180);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.MOVEMENT_SPEED, 0.06D);
    }

    public static boolean checkDumboOctopusSpawnRules(EntityType<DumboOctopusEntity> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, Random random) {
        return blockPos.getY() <= 40 && WaterAnimal.checkSurfaceWaterAnimalSpawnRules(entityType, levelAccessor, spawnType, blockPos, random);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RESTING, false);
        this.entityData.define(VARIANT, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new DumboOctopusEntity.RandomSwimmingGoal(this, 1.0D, 40));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Resting", this.isResting());
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setResting(compound.getBoolean("Resting"));
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.5F;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level.isClientSide() && this.isEffectiveAi()) {
            if (this.isInWater()) {
                if (this.isResting()) {
                    if (--this.restTimer <= 0) {
                        this.setResting(false);
                    }
                    this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.01D, 0.0D));
                } else if (this.random.nextFloat() <= 0.001F) {
                    this.restTimer = this.random.nextInt(200, 601);
                    this.setResting(true);
                }
            } else {
                this.setResting(false);
            }
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public void travel(Vec3 speed) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), speed);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(speed);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData, CompoundTag p_146750_) {
        this.setVariant(this.random.nextInt(0, 3));
        return spawnGroupData;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isResting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("dumbo_octopus_idle", true));
        } else if (this.isInWater()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("dumbo_octopus_swim", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("dumbo_octopus_on_land", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public boolean isResting() {
        return this.entityData.get(RESTING);
    }

    public void setResting(boolean resting) {
        this.entityData.set(RESTING, resting);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Mth.clamp(variant, 0, 2));
    }

    static class RandomSwimmingGoal extends net.minecraft.world.entity.ai.goal.RandomSwimmingGoal {
        private final DumboOctopusEntity dumboOctopus;

        public RandomSwimmingGoal(DumboOctopusEntity dumboOctopus, double speedModifier, int interval) {
            super(dumboOctopus, speedModifier, interval);
            this.dumboOctopus = dumboOctopus;
        }

        @Override
        public boolean canUse() {
            return !this.dumboOctopus.isResting() && super.canUse();
        }
    }

    class DumboOctopusMoveControl extends SmoothSwimmingMoveControl {
        public DumboOctopusMoveControl(int maxTurnX, int maxTurnY, float inWaterSpeedModifier, float outsideWaterSpeedModifier, boolean applyGravity) {
            super(DumboOctopusEntity.this, maxTurnX, maxTurnY, inWaterSpeedModifier, outsideWaterSpeedModifier, applyGravity);
        }

        @Override
        public void tick() {
            if (!DumboOctopusEntity.this.isResting()) {
                super.tick();
            }
        }
    }
}
