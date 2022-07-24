package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

public class SeaBunnyEntity extends WaterAnimal implements Bucketable, IAnimatable {
    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimationFactory factory = new AnimationFactory(this);

    public SeaBunnyEntity(EntityType<? extends SeaBunnyEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SeaBunnyMoveControl(this);
        this.jumpControl = new SeaBunnyJumpControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.08D);
    }

    public static boolean checkSeaBunnySpawnRules(EntityType<SeaBunnyEntity> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, Random random) {
        return blockPos.getY() > levelAccessor.getSeaLevel() - 32;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIMBING, false);
        this.entityData.define(VARIANT, 0);
        this.entityData.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SeaBunnyEntity.RandomStrollGoal(this, 1.0D));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Climbing", this.isClimbing());
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setClimbing(compound.getBoolean("Climbing"));
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    protected int getExperienceReward(Player player) {
        return this.random.nextInt(2, 5);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(ItemStack bucketStack) {
        CompoundTag bucketCompound = bucketStack.getOrCreateTag();
        Bucketable.saveDefaultDataToBucketTag(this, bucketStack);
        bucketCompound.putInt("BucketVariant", this.getVariant());
    }

    @Override
    public void loadFromBucketTag(CompoundTag bucketCompound) {
        Bucketable.loadDefaultDataFromBucketTag(this, bucketCompound);
        if (bucketCompound.contains("BucketVariant")) {
            this.setVariant(bucketCompound.getInt("BucketVariant"));
        }
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(CACItems.SEA_BUNNY_BUCKET.get());
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CACSounds.SEA_BUNNY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CACSounds.SEA_BUNNY_DEATH.get();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData, CompoundTag bucketCompound) {
        if (!mobSpawnType.equals(MobSpawnType.BUCKET) || bucketCompound == null || !bucketCompound.contains("BucketVariant")) {
            this.setVariant(this.random.nextInt(0, 3));
        }
        return super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, bucketCompound);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_AXOLOTL;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) {
            this.setClimbing(this.horizontalCollision && this.getNavigation().isInProgress());
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos) {
        return this.level.getBlockState(blockPos).getFluidState().isEmpty() ? 1.0F : 5.0F;
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    public void travel(Vec3 speed) {
        super.travel(speed);
        if (this.horizontalCollision && this.onClimbable()) {
            this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.12D, 0.0D));
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        return Bucketable.bucketMobPickup(player, interactionHand, this).orElse(super.mobInteract(player, interactionHand));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.animationSpeed > 0.03F) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sea_bunny_move", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sea_bunny", true));
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

    public boolean isClimbing() {
        return this.entityData.get(CLIMBING);
    }

    public void setClimbing(boolean climbing) {
        this.entityData.set(CLIMBING, climbing);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Mth.clamp(variant, 0, 2));
    }

    static class SeaBunnyMoveControl extends MoveControl {
        public SeaBunnyMoveControl(SeaBunnyEntity seaBunny) {
            super(seaBunny);
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
                double d0 = this.wantedX - this.mob.getX();
                double d2 = this.wantedZ - this.mob.getZ();
                float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;

                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 90.0F));
                this.mob.yBodyRot = this.mob.getYRot();

                float speed = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                speed *= this.mob.isInWater() ? 2.0F + this.speedModifier : this.speedModifier;
                this.mob.setSpeed(speed);
            } else {
                this.mob.setSpeed(0.0F);
            }
        }
    }

    static class SeaBunnyJumpControl extends JumpControl {
        public SeaBunnyJumpControl(SeaBunnyEntity seaBunny) {
            super(seaBunny);
        }

        @Override
        public void jump() {
        }
    }

    static class RandomStrollGoal extends net.minecraft.world.entity.ai.goal.RandomStrollGoal {
        public RandomStrollGoal(SeaBunnyEntity seaBunny, double speedModifier) {
            super(seaBunny, speedModifier);
        }

        @Override
        protected Vec3 getPosition() {
            Vec3 randomPos = RandomPos.generateRandomPos(this.mob, () -> {
                BlockPos blockPos = RandomPos.generateRandomDirection(this.mob.getRandom(), 10, 7);
                BlockPos blockPos1 = RandomPos.generateRandomPosTowardDirection(this.mob, 10, this.mob.getRandom(), blockPos);
                return RandomPos.moveUpOutOfSolid(blockPos1, this.mob.level.getMaxBuildHeight(), (blockPos2) -> GoalUtils.isSolid(this.mob, blockPos2));
            });
            return randomPos;
        }
    }
}
