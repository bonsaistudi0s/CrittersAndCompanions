package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LeafInsectEntity extends PathfinderMob implements GeoEntity {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(LeafInsectEntity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected BlockPos activeJukebox;
    protected boolean dancing;


    public LeafInsectEntity(EntityType<? extends LeafInsectEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    public static boolean checkLeafInsectSpawnRules(EntityType<LeafInsectEntity> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource random) {
        BlockState blockState = levelAccessor.getBlockState(blockPos.below());
        return blockPos.getY() > levelAccessor.getSeaLevel() - 16 && (blockState.is(BlockTags.DIRT) || blockState.is(BlockTags.LEAVES));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    public void aiStep() {
        if (this.activeJukebox == null || !this.activeJukebox.closerToCenterThan(this.position(), 5.0D) || !this.level().getBlockState(this.activeJukebox).is(Blocks.JUKEBOX)) {
            this.activeJukebox = null;
            this.dancing = false;
        }
        super.aiStep();
    }

    @Override
    public void setRecordPlayingNearby(BlockPos blockPos, boolean sounding) {
        this.activeJukebox = blockPos;
        this.dancing = sounding;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CACSounds.LEAF_INSECT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CACSounds.LEAF_INSECT_DEATH.get();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData, CompoundTag p_146750_) {
        this.setVariant(this.random.nextInt(0, 3));
        return spawnGroupData;
    }

    private PlayState predicate(AnimationState<?> event) {
        if (this.isDancing()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("leaf_insect_dance"));
        } else if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("leaf_insect_walk"));
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("leaf_insect_idle"));
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

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Mth.clamp(variant, 0, 2));
    }

    public boolean isDancing() {
        return this.dancing;
    }

    static class WaterAvoidingRandomStrollGoal extends net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal {
        private final LeafInsectEntity leafInsect;

        public WaterAvoidingRandomStrollGoal(LeafInsectEntity leafInsect, double speedModifier) {
            super(leafInsect, speedModifier);
            this.leafInsect = leafInsect;
        }

        @Override
        public boolean canUse() {
            return !leafInsect.isDancing() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !leafInsect.isDancing() && super.canContinueToUse();
        }
    }
}
