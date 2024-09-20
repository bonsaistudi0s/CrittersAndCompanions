package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;

public class OtterEntity extends Animal implements GeoEntity {
    private static final EntityDataAccessor<Boolean> FLOATING = SynchedEntityData.defineId(OtterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(OtterEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean needsSurface;
    private int huntDelay;
    private int eatDelay;
    private int eatTime;
    private int floatTime;

    public OtterEntity(EntityType<? extends OtterEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new OtterMoveControl(this);
        this.lookControl = new OtterLookControl(this);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setCanPickUpLoot(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    public static boolean checkOtterSpawnRules(EntityType<OtterEntity> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource random) {
        return blockPos.getY() > levelAccessor.getSeaLevel() - 16;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLOATING, false);
        this.entityData.define(EATING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, Player.class, 32.0F, 0.9D, 1.5D, (livingEntity -> livingEntity.equals(this.getLastHurtMob()))));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(2, new GoToSurfaceGoal(60));
        this.goalSelector.addGoal(3, new BreedGoal(this));
        this.goalSelector.addGoal(4, new SearchFoodGoal());
        this.goalSelector.addGoal(5, new FollowParentGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractFish.class, 20, false, false, (fish) -> fish instanceof AbstractSchoolingFish && this.getHuntDelay() <= 0));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("HuntDelay", this.getHuntDelay());
        compound.putBoolean("Floating", this.isFloating());
        compound.putInt("FloatTime", this.floatTime);
        compound.putBoolean("Eating", this.isEating());
        compound.putInt("EatTime", this.eatTime);
        compound.putInt("EatDelay", this.eatDelay);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.huntDelay = compound.getInt("HuntDelay");
        this.setFloating(compound.getBoolean("Floating"));
        this.floatTime = compound.getInt("FloatTime");
        this.setEating(compound.getBoolean("Eating"));
        this.eatTime = compound.getInt("EatTime");
        this.eatDelay = compound.getInt("EatDelay");
    }

    @Override
    public void awardKillScore(Entity killedEntity, int i, DamageSource damageSource) {
        super.awardKillScore(killedEntity, i, damageSource);
        if (killedEntity instanceof AbstractSchoolingFish) {
            this.huntDelay = 6000;
        }
    }

    @Override
    public int getExperienceReward() {
        return this.random.nextInt(3, 7);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getLastHurtMob() != null) {
            if (this.tickCount - this.getLastHurtMobTimestamp() > 100) {
                this.setLastHurtMob(null);
            }
        }

        if (this.tickCount % 60 == 0) {
            heal(0.5F);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isEffectiveAi()) {
            if (this.isFloating()) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
                this.setYya(0.0F);
                this.setAirSupply(this.getMaxAirSupply());

                if (--this.floatTime <= 0) {
                    this.setFloating(false);
                }
            }

            if (this.isUnderWater() && (this.getAirSupply() < 200 || this.random.nextFloat() <= 0.001F)) {
                this.setNeedsSurface(true);
            }

            if (this.isEating()) {
                if (this.eatDelay > 0) {
                    --this.eatDelay;
                } else {
                    Vec3 mouthPos = this.calculateMouthPos();
                    ((ServerLevel) this.level()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, this.getMainHandItem()), mouthPos.x(), mouthPos.y(), mouthPos.z(), 2, 0.0D, 0.1D, 0.0D, 0.05D);

                    if (this.getRandom().nextDouble() < 0.5D) {
                        this.playSound(CACSounds.OTTER_EAT.get(), 1.2F, 1.0F);
                    }
                    if (--this.eatTime <= 0) {
                        this.eat(this.level(), this.getMainHandItem());
                        this.setEating(false);
                    }
                }
            } else {
                if (this.isFood(this.getMainHandItem())) {
                    if (this.isInWater()) {
                        if (this.isFloating()) {
                            this.startEating();
                        } else {
                            this.setNeedsSurface(true);
                        }
                    } else if (this.onGround()) {
                        this.startEating();
                    }
                }
            }

            if (this.huntDelay > 0) {
                --this.huntDelay;
            }
        }
    }

    @Override
    public ItemStack eat(Level level, ItemStack itemStack) {
        if (itemStack.is(CACItems.CLAM.get())) {
            if (this.random.nextFloat() <= 0.07F) {
                Vec3 mouthPos = this.calculateMouthPos();
                ItemEntity pearl = new ItemEntity(level, mouthPos.x(), mouthPos.y(), mouthPos.z(), new ItemStack(CACItems.PEARL.get()));

                pearl.setDeltaMovement(this.getRandom().nextGaussian() * 0.05D, this.getRandom().nextGaussian() * 0.05D + 0.2D, this.getRandom().nextGaussian() * 0.05D);
                level.addFreshEntity(pearl);
            }
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.TURTLE_EGG_BREAK, SoundSource.NEUTRAL, 0.8F, 1.5F);
            itemStack.shrink(1);
            return itemStack;
        } else {
            return super.eat(level, itemStack);
        }
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.6F : 1.0F;
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack itemStack = itemEntity.getItem();

        if (this.rejectedItem(itemEntity)) {
            return;
        }

        ItemStack itemsEquipped = this.equipItemIfPossible(itemStack);
        if (!itemsEquipped.isEmpty()) {
            int count = itemsEquipped.getCount();

            if (count > 1) {
                var leftover = itemStack.copy();
                leftover.shrink(itemsEquipped.getCount());
                ItemEntity extraItems = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), leftover);
                this.level().addFreshEntity(extraItems);
            }
            this.onItemPickup(itemEntity);
            this.take(itemEntity, itemsEquipped.getCount());
            itemEntity.discard();
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new OtterPathNavigation(this, level);
    }

    @Override
    public int getMaxAirSupply() {
        return 9600;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> fluidTag) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double) 0.08F * this.getAttribute(ForgeMod.SWIM_SPEED.get()).getValue(), 0.0D));
    }
    

    @Override
    public void travel(Vec3 speed) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), speed);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            this.calculateEntityAnimation( false);
        } else {
            super.travel(speed);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack handStack = player.getItemInHand(interactionHand);
        if (!this.isEating() && this.isFood(handStack)) {
            this.setItemInHand(InteractionHand.MAIN_HAND, handStack.split(1));
            handStack.shrink(1);
            return super.mobInteract(player, interactionHand);
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean canHoldItem(ItemStack itemStack) {
        return this.isFood(itemStack) && this.isHungryAt(itemStack);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return (stack.isEdible() && stack.is(ItemTags.FISHES)) || stack.is(CACItems.CLAM.get());
    }

    @Override
    public boolean canBreed() {
        return !this.isBaby();
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
        OtterEntity otter = CACEntities.OTTER.get().create(level);
        return otter;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (super.doHurtTarget(entity)) {
            this.playSound(CACSounds.BITE_ATTACK.get(), this.getSoundVolume(), this.getVoicePitch());
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return CACSounds.OTTER_AMBIENT.get();
    }

    @Override
    protected SoundEvent getSwimSound() {
        return CACSounds.OTTER_SWIM.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CACSounds.OTTER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CACSounds.OTTER_DEATH.get();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData, CompoundTag p_146750_) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, p_146750_);
        if (mobSpawnType.equals(MobSpawnType.SPAWNER) && this.random.nextFloat() <= 0.2F) {
            for (int i = 0; i < this.random.nextInt(1, 4); i++) {
                OtterEntity baby = CACEntities.OTTER.get().create(this.level());
                baby.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                baby.setBaby(true);
                levelAccessor.addFreshEntity(baby);
            }
        }
        return spawnGroupData;
    }

    private PlayState predicate(AnimationState<?> event) {
        if (this.isInWater()) {
            if (this.isFloating()) {
                event.getController().setAnimation(RawAnimation.begin().thenLoop("otter_float"));
            } else {
                event.getController().setAnimation(RawAnimation.begin().thenLoop("otter_swim"));
            }
            return PlayState.CONTINUE;
        } else {
            if (this.isEating()) {
                if (this.getMainHandItem().is(CACItems.CLAM.get())) {
                    event.getController().setAnimation(RawAnimation.begin().then("otter_open", Animation.LoopType.PLAY_ONCE));
                } else {
                    event.getController().setAnimation(RawAnimation.begin().then("otter_standing_eat", Animation.LoopType.PLAY_ONCE));
                }
                return PlayState.CONTINUE;
            } else if (event.isMoving()) {
                event.getController().setAnimation(RawAnimation.begin().thenLoop("otter_walk"));
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(RawAnimation.begin().thenLoop("otter_idle"));
                return PlayState.CONTINUE;
            }
        }
    }

    private PlayState floatingHandsPredicate(AnimationState<?> event) {
        if (this.isFloating()) {
            if (this.isEating() && this.eatDelay <= 0) {
                event.getController().setAnimation(RawAnimation.begin().then("otter_hands_float_eat", Animation.LoopType.PLAY_ONCE));
            } else {
                event.getController().setAnimation(RawAnimation.begin().thenLoop("otter_hands_float_idle"));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 20, this::predicate));
        controllers.add(new AnimationController<>(this, "floating_hands_controller", 10, this::floatingHandsPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public boolean isHungryAt(ItemStack foodStack) {
        return foodStack.is(CACItems.CLAM.get()) || this.getInLoveTime() <= 0;
    }

    private void rejectFood() {
        if (!this.getMainHandItem().isEmpty()) {
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getMainHandItem().copy());
            itemEntity.setPickUpDelay(40);
            itemEntity.setThrower(this.getUUID());
            this.getMainHandItem().shrink(1);
            this.level().addFreshEntity(itemEntity);
        }
    }

    public boolean rejectedItem(ItemEntity itemEntity) {
        if (itemEntity.getOwner() != null) {
            return itemEntity.getOwner().equals(this.getUUID());
        }
        return false;
    }

    private void startEating() {
        if (this.isFood(this.getMainHandItem())) {
            this.eatDelay = this.getMainHandItem().is(CACItems.CLAM.get()) ? 35 : 12;
            this.eatTime = 20;
            this.setEating(true);
        }
    }

    private void startFloating(int time) {
        this.floatTime = time;
        this.setFloating(true);
    }

    public Vec3 calculateMouthPos() {
        Vec3 viewVector = this.getViewVector(0.0F).scale(this.isFloating() ? 0.3D : 0.6D).add(0.0D, this.isFloating() ? 0.55D : 0.0D, 0.0D).scale(this.getScale());
        return new Vec3(this.getX() + viewVector.x(), this.getY() + viewVector.y(), this.getZ() + viewVector.z());
    }

    public int getHuntDelay() {
        return huntDelay;
    }

    public boolean needsSurface() {
        return this.needsSurface;
    }

    public void setNeedsSurface(boolean needsSurface) {
        this.needsSurface = needsSurface;
    }

    public boolean isEating() {
        return this.entityData.get(EATING);
    }

    public void setEating(boolean eating) {
        this.entityData.set(EATING, eating);
    }

    public boolean isFloating() {
        return this.entityData.get(FLOATING);
    }

    public void setFloating(boolean floating) {
        this.entityData.set(FLOATING, floating);
    }

    static class OtterMoveControl extends MoveControl {
        private final OtterEntity otter;

        public OtterMoveControl(OtterEntity otterEntity) {
            super(otterEntity);
            this.otter = otterEntity;
        }

        @Override
        public void tick() {
            if (this.otter.isInWater()) {
                if (!this.otter.needsSurface()) {
                    this.otter.setDeltaMovement(this.otter.getDeltaMovement().add(this.otter.getLookAngle().scale(this.otter.isFloating() ? 0.002F : 0.005F)));
                }

                if (!this.otter.isFloating()) {
                    if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
                        double d0 = this.wantedX - this.mob.getX();
                        double d1 = this.wantedY - this.mob.getY();
                        double d2 = this.wantedZ - this.mob.getZ();
                        double distanceSqr = d0 * d0 + d1 * d1 + d2 * d2;

                        if (distanceSqr < (double) 2.5000003E-7F) {
                            this.mob.setZza(0.0F);
                        } else {
                            float yRot = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRot, 40.0F));
                            this.mob.yBodyRot = this.mob.getYRot();
                            this.mob.yHeadRot = this.mob.getYRot();
                            float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                            this.mob.setSpeed(speed * 0.2F);

                            double horizontalDistance = Math.sqrt(d0 * d0 + d2 * d2);
                            if (Math.abs(d1) > (double) 1.0E-5F || Math.abs(horizontalDistance) > (double) 1.0E-5F) {
                                float xRot = -((float) (Mth.atan2(d1, horizontalDistance) * (double) (180F / (float) Math.PI)));
                                xRot = Mth.clamp(Mth.wrapDegrees(xRot), -180.0F, 180.0F);
                                this.mob.setXRot(this.rotlerp(this.mob.getXRot(), xRot, 45.0F));
                            }

                            BlockPos wantedPos = BlockPos.containing(this.wantedX, this.wantedY, this.wantedZ);
                            BlockState wantedBlockState = this.mob.level().getBlockState(wantedPos);

                            if (d1 > (double) this.mob.maxUpStep() && d0 * d0 + d2 * d2 < 4.0F && d1 <= 1.0D && wantedBlockState.getFluidState().isEmpty()) {
                                this.mob.getJumpControl().jump();
                                this.mob.setSpeed(speed);
                            }

                            float f0 = Mth.cos(this.mob.getXRot() * ((float) Math.PI / 180F));
                            float f1 = Mth.sin(this.mob.getXRot() * ((float) Math.PI / 180F));
                            this.mob.zza = f0 * speed;
                            this.mob.yya = -f1 * (speed);
                        }
                    } else {
                        this.mob.setSpeed(0.0F);
                        this.mob.setXxa(0.0F);
                        this.mob.setYya(0.0F);
                        this.mob.setZza(0.0F);
                    }
                }
            } else {
                super.tick();
            }
        }
    }

    static class OtterLookControl extends LookControl {
        private final OtterEntity otter;

        public OtterLookControl(OtterEntity otterEntity) {
            super(otterEntity);
            this.otter = otterEntity;
        }

        @Override
        public void tick() {
            if (this.otter.isInWater()) {
                if (this.lookAtCooldown > 0) {
                    --this.lookAtCooldown;
                    this.getYRotD().ifPresent((p_181134_) -> {
                        this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, p_181134_ + 20.0F, this.yMaxRotSpeed);
                    });
                    this.getXRotD().ifPresent((p_181132_) -> {
                        this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), p_181132_ + 10.0F, this.xMaxRotAngle));
                    });
                } else {
                    if (this.mob.getNavigation().isDone()) {
                        this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), 0.0F, 5.0F));
                    }

                    this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
                }
            } else {
                super.tick();
            }
        }
    }

    static class OtterPathNavigation extends WaterBoundPathNavigation {
        private final OtterEntity otter;

        public OtterPathNavigation(OtterEntity otterEntity, Level level) {
            super(otterEntity, level);
            this.otter = otterEntity;
        }

        @Override
        protected PathFinder createPathFinder(int p_26531_) {
            this.nodeEvaluator = new AmphibiousNodeEvaluator(true);
            return new PathFinder(this.nodeEvaluator, p_26531_);
        }

        @Override
        protected Vec3 getTempMobPos() {
            return new Vec3(this.otter.getX(), this.otter.getY(0.5D), this.otter.getZ());
        }

        @Override
        protected boolean canUpdatePath() {
            return true;
        }

        @Override
        public boolean isStableDestination(BlockPos destination) {
            if (this.otter.isInWater() && this.level.getBlockState(destination).isAir()) {
                return !(this.level.getBlockState(destination.below()).isAir() || this.level.getBlockState(destination.below()).getFluidState().is(FluidTags.WATER));
            } else {
                return !this.level.getBlockState(destination.below()).isAir();
            }
        }
    }

    static class BreedGoal extends net.minecraft.world.entity.ai.goal.BreedGoal {
        private final OtterEntity otter;

        public BreedGoal(OtterEntity otterEntity) {
            super(otterEntity, 1.0D);
            this.otter = otterEntity;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.otter.isEating();
        }
    }

    static class RandomLookAroundGoal extends net.minecraft.world.entity.ai.goal.RandomLookAroundGoal {
        private final OtterEntity otter;

        public RandomLookAroundGoal(OtterEntity otterEntity) {
            super(otterEntity);
            this.otter = otterEntity;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.otter.isInWater() && !this.otter.isEating();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.otter.isInWater() && !this.otter.isEating();
        }
    }

    static class RandomStrollGoal extends net.minecraft.world.entity.ai.goal.RandomStrollGoal {
        private final OtterEntity otter;

        public RandomStrollGoal(OtterEntity otterEntity) {
            super(otterEntity, 1.0F, 20);
            this.otter = otterEntity;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !(this.otter.isFloating() || this.otter.needsSurface() || this.otter.isEating());
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !(this.otter.isFloating() || this.otter.needsSurface() || this.otter.isEating());
        }
    }

    static class FollowParentGoal extends net.minecraft.world.entity.ai.goal.FollowParentGoal {
        private final OtterEntity otter;

        public FollowParentGoal(OtterEntity otterEntity) {
            super(otterEntity, 1.2D);
            this.otter = otterEntity;
        }

        @Override
        public boolean canUse() {
            return !this.otter.isEating() && super.canUse();
        }
    }

    static class LookAtPlayerGoal extends net.minecraft.world.entity.ai.goal.LookAtPlayerGoal {
        private final OtterEntity otter;

        public LookAtPlayerGoal(OtterEntity otterEntity) {
            super(otterEntity, Player.class, 8.0F);
            this.otter = otterEntity;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !(this.otter.isInWater() || this.otter.isEating());
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !(this.otter.isInWater() || this.otter.isEating());
        }
    }

    public class GoToSurfaceGoal extends Goal {
        private final int timeoutTime;
        private boolean goingLand;
        private Vec3 targetPos;
        private int timeoutTimer;

        public GoToSurfaceGoal(int timeoutTime) {
            this.timeoutTime = timeoutTime;
            this.timeoutTimer = timeoutTime;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return OtterEntity.this.isAlive() && OtterEntity.this.needsSurface() && !OtterEntity.this.onGround();
        }

        @Override
        public void start() {
            if (OtterEntity.this.getMainHandItem().is(CACItems.CLAM.get())) {
                this.targetPos = LandRandomPos.getPos(OtterEntity.this, 7, 15);
                this.goingLand = true;
            } else {
                this.targetPos = this.findAirPosition();
                this.goingLand = false;
            }
        }

        @Override
        public void tick() {
            if (this.targetPos == null || !OtterEntity.this.level().getBlockState(BlockPos.containing(this.targetPos)).isAir()) {
                if (OtterEntity.this.getMainHandItem().is(CACItems.CLAM.get())) {
                    this.targetPos = LandRandomPos.getPos(OtterEntity.this, 15, 7);
                    this.goingLand = true;
                } else {
                    this.targetPos = this.findAirPosition();
                    this.goingLand = false;
                }
                this.tickTimeout();
            } else {
                OtterEntity.this.getNavigation().moveTo(this.targetPos.x(), this.targetPos.y(), this.targetPos.z(), 1.0D);
                OtterEntity.this.moveRelative(0.02F, new Vec3(OtterEntity.this.xxa, OtterEntity.this.yya, OtterEntity.this.zza));
                OtterEntity.this.move(MoverType.SELF, OtterEntity.this.getDeltaMovement());

                if (this.goingLand) {
                    if (!OtterEntity.this.isInWater() && OtterEntity.this.onGround()) {
                        this.stop();
                    }
                } else {
                    if (this.targetPos.y() > OtterEntity.this.getY() && targetPos.distanceToSqr(OtterEntity.this.position()) <= 3.0D) {
                        OtterEntity.this.push(0.0D, 0.02D, 0.0D);
                    }

                    double d0 = this.targetPos.y() - OtterEntity.this.getEyePosition().y();
                    if (Math.sqrt(d0 * d0) <= 0.1D) {
                        OtterEntity.this.setDeltaMovement(OtterEntity.this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
                        OtterEntity.this.setYya(0.0F);
                        OtterEntity.this.setSpeed(0.0F);
                        OtterEntity.this.startFloating(OtterEntity.this.getRandom().nextInt(80, 201));
                        this.stop();
                    }
                }
            }
        }

        public void tickTimeout() {
            if (this.timeoutTimer % 2 == 0) {
                ((ServerLevel) OtterEntity.this.level()).sendParticles(ParticleTypes.BUBBLE, OtterEntity.this.getRandomX(0.6D), OtterEntity.this.getY(), OtterEntity.this.getRandomZ(0.6D), 2, 0.0D, 0.1D, 0.0D, 0.0D);
            }
            if (this.timeoutTimer <= 0) {
                OtterEntity.this.playSound(CACSounds.OTTER_AMBIENT.get(), OtterEntity.this.getSoundVolume(), 0.3F);
                OtterEntity.this.rejectFood();
                this.stop();
            }
            --this.timeoutTimer;
        }

        @Override
        public void stop() {
            OtterEntity.this.setNeedsSurface(false);
            OtterEntity.this.getNavigation().stop();
            this.timeoutTimer = this.timeoutTime;
        }

        private Vec3 findAirPosition() {
            Iterable<BlockPos> blocksInRadius = BlockPos.betweenClosed(Mth.floor(OtterEntity.this.getX() - 1.0D), OtterEntity.this.getBlockY(), Mth.floor(OtterEntity.this.getZ() - 1.0D), Mth.floor(OtterEntity.this.getX() + 1.0D), Mth.floor(OtterEntity.this.getY() + 16.0D), Mth.floor(OtterEntity.this.getZ() + 1.0D));
            BlockPos airPos = null;

            for (BlockPos blockPos : blocksInRadius) {
                if (OtterEntity.this.level().getBlockState(blockPos).isAir()) {
                    airPos = blockPos;
                    break;
                }
            }

            return airPos != null ? Vec3.atBottomCenterOf(airPos) : null;
        }
    }

    public class SearchFoodGoal extends Goal {
        public SearchFoodGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!OtterEntity.this.getMainHandItem().isEmpty()) {
                return false;
            } else {
                List<ItemEntity> itemsInRadius = OtterEntity.this.level().getEntitiesOfClass(ItemEntity.class, OtterEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> OtterEntity.this.wantsToPickUp(itemEntity.getItem()) && !OtterEntity.this.rejectedItem(itemEntity)));
                return !itemsInRadius.isEmpty();
            }
        }

        @Override
        public void tick() {
            List<ItemEntity> itemsInRadius = OtterEntity.this.level().getEntitiesOfClass(ItemEntity.class, OtterEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> OtterEntity.this.wantsToPickUp(itemEntity.getItem()) && !OtterEntity.this.rejectedItem(itemEntity)));
            ItemStack handStack = OtterEntity.this.getMainHandItem();
            if (handStack.isEmpty() && !itemsInRadius.isEmpty()) {
                Path path = OtterEntity.this.getNavigation().createPath(itemsInRadius.get(0), 0);
                OtterEntity.this.getNavigation().moveTo(path, 1.0D);
            }
        }

        @Override
        public void start() {
            List<ItemEntity> itemsInRadius = OtterEntity.this.level().getEntitiesOfClass(ItemEntity.class, OtterEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> OtterEntity.this.wantsToPickUp(itemEntity.getItem()) && !OtterEntity.this.rejectedItem(itemEntity)));
            if (!itemsInRadius.isEmpty()) {
                Path path = OtterEntity.this.getNavigation().createPath(itemsInRadius.get(0), 0);
                OtterEntity.this.getNavigation().moveTo(path, 1.0D);
            }
        }
    }
}
