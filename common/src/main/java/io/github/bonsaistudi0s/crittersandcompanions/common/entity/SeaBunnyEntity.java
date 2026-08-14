package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.base.AgeableWaterAnimal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BabyHealthPenaltyBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviours;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.ClimbingBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.VariantBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.control.NoJumpControl;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.control.SeaBunnyMoveControl;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SeaBunnyEntity extends AgeableWaterAnimal implements Bucketable, GeoEntity {

    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.BOOLEAN);

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.SEA_BUNNY.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected int harvestCooldown;

    public SeaBunnyEntity(EntityType<? extends SeaBunnyEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SeaBunnyMoveControl(this);
        this.jumpControl = new NoJumpControl(this);
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
        behaviours.add(new ClimbingBehaviour(this, CLIMBING));
        behaviours.add(new BabyHealthPenaltyBehaviour(this));
    }

    @Override
    public @Nullable SeaBunnyEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        var baby = CACEntities.SEA_BUNNY.get().create(level);
        if (baby == null) {
            return null;
        }

        if (otherParent instanceof SeaBunnyEntity otherSeaBunnyParent) {
            baby.behaviour(VariantBehaviour.class).inherit(this, otherSeaBunnyParent);
        }

        return baby;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new PanicGoal(this, 1.5D));
        goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        goalSelector.addGoal(2, TAGS.temptGoal(this));
        goalSelector.addGoal(3, new FollowParentGoal(this, 1.25D));
        goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public int getExperienceReward() {
        return this.random.nextInt(2, 5);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(TAGS.food());
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
        var tag = bucketStack.getOrCreateTag();
        getBehaviours().forEach(it -> it.save(tag));
        //noinspection deprecation
        Bucketable.saveDefaultDataToBucketTag(this, bucketStack);
    }

    @Override
    public void loadFromBucketTag(CompoundTag bucketCompound) {
        //noinspection deprecation
        Bucketable.loadDefaultDataFromBucketTag(this, bucketCompound);
        getBehaviours().forEach(it -> it.read(bucketCompound));
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
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos) {
        return this.level().getBlockState(blockPos).getFluidState().isEmpty() ? 1.0F : 5.0F;
    }

    @Override
    public boolean onClimbable() {
        return behaviour(ClimbingBehaviour.class).isClimbing();
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack handStack = player.getItemInHand(hand);
        if (handStack.is(Items.BUCKET) || handStack.is(Items.WATER_BUCKET)) {
            return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
        } else if (handStack.is(Items.GLASS_BOTTLE)) {
            if (this.harvestCooldown <= 0 && !isBaby()) {
                handStack.shrink(1);
                this.level().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.8F);
                if (handStack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()));
                } else if (!player.getInventory().add(new ItemStack(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()))) {
                    player.drop(new ItemStack(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()), false);
                }
                this.harvestCooldown = 6000;
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
        }
        return super.mobInteract(player, hand);
    }

    private PlayState predicate(AnimationState<?> event) {
        if (this.isBaby()) {
            return PlayState.CONTINUE;
        }

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

    public static boolean checkSeaBunnySpawnRules(EntityType<SeaBunnyEntity> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        int i = level.getSeaLevel();
        int j = i - 13;
        return pos.getY() >= j && pos.getY() <= i && level.getFluidState(pos).is(FluidTags.WATER) && level.getBlockState(pos.above()).is(
                Blocks.WATER) && level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
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
