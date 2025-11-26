package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.Behaviours;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.VariantBehaviour;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SeaBunnyEntity extends WaterAnimal implements Bucketable, GeoEntity {
    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected int harvestCooldown;

    public SeaBunnyEntity(EntityType<? extends SeaBunnyEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SeaBunnyMoveControl(this);
        this.jumpControl = new SeaBunnyJumpControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.08D);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new VariantBehaviour(this, VARIANT, 3));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIMBING, false);
        builder.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Climbing", this.isClimbing());
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setClimbing(compound.getBoolean("Climbing"));
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public int getBaseExperienceReward() {
        return this.random.nextInt(2, 5);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.harvestCooldown > 0) {
            this.harvestCooldown--;
        }
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
        Bucketable.saveDefaultDataToBucketTag(this, bucketStack);
    }

    @Override
    public void loadFromBucketTag(CompoundTag bucketCompound) {
        Bucketable.loadDefaultDataFromBucketTag(this, bucketCompound);
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
        if (!this.level().isClientSide()) {
            this.setClimbing(this.horizontalCollision && this.getNavigation().isInProgress());
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos) {
        return this.level().getBlockState(blockPos).getFluidState().isEmpty() ? 1.0F : 5.0F;
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
        ItemStack handStack = player.getItemInHand(interactionHand);
        if (handStack.is(Items.BUCKET) || handStack.is(Items.WATER_BUCKET)) {
            return Bucketable.bucketMobPickup(player, interactionHand, this).orElse(super.mobInteract(player, interactionHand));
        } else if (handStack.is(Items.GLASS_BOTTLE)) {
            if (this.harvestCooldown <= 0) {
                handStack.shrink(1);
                this.level().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.8F);
                if (handStack.isEmpty()) {
                    player.setItemInHand(interactionHand, new ItemStack(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()));
                } else if (!player.getInventory().add(new ItemStack(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()))) {
                    player.drop(new ItemStack(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()), false);
                }
                this.harvestCooldown = 6000;
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
        }
        return super.mobInteract(player, interactionHand);
    }

    private PlayState predicate(AnimationState<?> event) {
        if (this.getSpeed() > 0.03F) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("sea_bunny_move"));
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("sea_bunny"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public boolean isClimbing() {
        return this.entityData.get(CLIMBING);
    }

    public void setClimbing(boolean climbing) {
        this.entityData.set(CLIMBING, climbing);
    }

    static class SeaBunnyMoveControl extends MoveControl {
        public SeaBunnyMoveControl(SeaBunnyEntity seaBunny) {
            super(seaBunny);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
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
            super(seaBunny, speedModifier, 20);
        }

        @Override
        protected Vec3 getPosition() {
            Vec3 randomPos = RandomPos.generateRandomPos(this.mob, () -> {
                BlockPos dirPos = RandomPos.generateRandomDirection(this.mob.getRandom(), 2, 2);
                BlockPos dirRandomPos = RandomPos.generateRandomPosTowardDirection(this.mob, 2, this.mob.getRandom(), dirPos);
                BlockPos finalPos = RandomPos.moveUpOutOfSolid(dirRandomPos, this.mob.level().getMaxBuildHeight(), (blockPos) -> GoalUtils.isSolid(this.mob, blockPos));
                return this.mob.level().getBlockState(finalPos).getFluidState().isEmpty() ? null : finalPos;
            });
            return randomPos;
        }
    }
}
