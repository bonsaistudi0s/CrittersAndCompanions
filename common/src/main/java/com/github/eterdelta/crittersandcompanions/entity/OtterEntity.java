package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.entity.brain.OtterNavigation;
import com.github.eterdelta.crittersandcompanions.entity.brain.OtterNodeEvaluator;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
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
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
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
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class OtterEntity extends Animal implements GeoEntity {
    private static final EntityDataAccessor<Boolean> FLOATING = SynchedEntityData.defineId(OtterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(OtterEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final Vec3i UNDERWATER_PICKUP_REACH = new Vec3i(1, 1, 1);

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
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractFish.class, 20, false, false, (fish) -> fish instanceof AbstractSchoolingFish && this.getHuntDelay() <= 0 && this.getMainHandItem().isEmpty()));
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
    public void tick() {
        super.tick();

        // Forces body local pitch to 0 when out of water
        if (!this.isInWater()) {
            this.setXRot(0);
            this.xRotO = 0;
        }

    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isControlledByLocalInstance()) {
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
                    ((ServerLevel) this.level()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, this.getMainHandItem().copy()), mouthPos.x(), mouthPos.y(), mouthPos.z(), 2, 0.0D, 0.1D, 0.0D, 0.05D);

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
        if (this.rejectedItem(itemEntity)) {
            return;
        }

        super.pickUpItem(itemEntity);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new OtterNavigation(this, level);

    }

    @Override
    public int getMaxAirSupply() {
        return 9600;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> fluidTag) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double) 0.08F * this.getAttribute(Services.PLATFORM.getSwimSpeedAttribute()).getValue(), 0.0D));
    }


    @Override
    public void travel(Vec3 speed) {
        if (this.isControlledByLocalInstance() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), speed);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            this.calculateEntityAnimation(false);
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
        if (isFloating()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("otter_float"));
            return PlayState.CONTINUE;
        } else if (isInWater()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("otter_swim"));
            return PlayState.CONTINUE;
        } else {
            if (isEating()) {
                if (getMainHandItem().is(CACItems.CLAM.get())) {
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
        if (isFloating()) {
            if (isEating() && eatDelay <= 0) {
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
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
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
            ItemStack thrownAway = this.getMainHandItem().copy();
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), thrownAway);
            itemEntity.setPickUpDelay(40);
            itemEntity.setThrower(this.getUUID());
            this.getMainHandItem().shrink(thrownAway.getCount());
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

    @Override
    public int getMaxFallDistance() {
        if (isUnderWater()) return 16;
        return super.getMaxFallDistance();
    }

    @Override
    protected Vec3i getPickupReach() {
        if (isUnderWater()) return UNDERWATER_PICKUP_REACH;
        return super.getPickupReach();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    private boolean isReadyToFloat() {
        BlockPos eye = BlockPos.containing(this.getX(), this.getEyeY(), this.getZ());
        return !this.isUnderWater() && this.level().getBlockState(eye).isAir() && this.level().getFluidState(eye.below()).is(FluidTags.WATER);
    }

    private Vec3 findSurfacePosStraightUp() {
        BlockPos.MutableBlockPos curr = new BlockPos.MutableBlockPos(Mth.floor(this.getX()), Mth.floor(this.getEyeY()), Mth.floor(this.getZ()));

        boolean waterInSight = false;
        for (int i = 0; i < 40; i++) {
            BlockPos pos = curr.above(i);
            if (this.level().getFluidState(pos).is(FluidTags.WATER)) {
                waterInSight = true;
                continue;
            }

            if (waterInSight && this.level().getBlockState(pos).isAir()) {
                return Vec3.atCenterOf(pos).add(0.0D, 0.1D, 0.0D);
            }

        }

        return null;
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

                                // Fallback for the otter going to the abyss of the sea after hunting a fish when it failed to float (although this should not happen)
                                if (this.otter.needsSurface() && xRot > 0.0F) {
                                    xRot = 0.0F;
                                }

                                this.mob.setXRot(this.rotlerp(this.mob.getXRot(), xRot, 45.0F));
                            }

                            BlockPos wantedPos = BlockPos.containing(this.wantedX, this.wantedY, this.wantedZ);
                            BlockState wantedBlockState = this.mob.level().getBlockState(wantedPos);

                            if (d1 > 0.6 && d0 * d0 + d2 * d2 < 4.0F && d1 <= 1.0D && wantedBlockState.getFluidState().isEmpty()) {
                                this.mob.getJumpControl().jump();

                                // Decreased speed factor if it's inside water
                                float waterFactor = 0.14F;
                                if (this.otter.needsSurface()) {
                                    waterFactor = 0.08F;
                                }

                                this.mob.setSpeed(speed * waterFactor);
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

    // No longer used
    static class OtterPathNavigation extends WaterBoundPathNavigation {
        private final OtterEntity otter;

        public OtterPathNavigation(OtterEntity otterEntity, Level level) {
            super(otterEntity, level);
            this.otter = otterEntity;
        }

        @Override
        protected PathFinder createPathFinder(int p_26531_) {
            this.nodeEvaluator = new OtterNodeEvaluator();
            return new PathFinder(this.nodeEvaluator, p_26531_);
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
        private Vec3 targetPos;
        private int timeoutTimer;

        private int stuckTicks;
        private double lastDist = Double.MAX_VALUE;

        public GoToSurfaceGoal(int timeoutTime) {
            this.timeoutTime = timeoutTime;
            this.timeoutTimer = timeoutTime;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return OtterEntity.this.isAlive() && OtterEntity.this.needsSurface() && !OtterEntity.this.onGround() && !OtterEntity.this.isFloating();
        }

        private void searchTargetPos() {
            if (OtterEntity.this.isInWater() && OtterEntity.this.isFood(OtterEntity.this.getMainHandItem())) {
                Vec3 surface = OtterEntity.this.findSurfacePosStraightUp();
                if (surface != null) {
                    this.targetPos = surface;
                    return;
                }

            }

            Vec3 surface = OtterEntity.this.findSurfacePosStraightUp();
            if (surface != null) {
                this.targetPos = surface;
                return;
            }

            this.targetPos = findAirPosition();
        }

        @Override
        public void start() {
            this.stuckTicks = 0;
            this.lastDist = Double.MAX_VALUE;
            searchTargetPos();
        }

        @Override
        public void tick() {
            // Embed goals sometimes computee client sided ticks due to a sync error, although this happens in singleplayer
            // it's better to encapsulate the method
            if (OtterEntity.this.level().isClientSide) {
                return;
            }

            if (OtterEntity.this.isReadyToFloat()) {
                OtterEntity.this.setDeltaMovement(OtterEntity.this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
                OtterEntity.this.setYya(0.0F);
                OtterEntity.this.setSpeed(0.0F);

                OtterEntity.this.startFloating(OtterEntity.this.getRandom().nextInt(80, 201));

                this.stop();

                return;
            }

            if (this.targetPos == null || !OtterEntity.this.level().getBlockState(BlockPos.containing(this.targetPos)).isAir()) {
                searchTargetPos();
                this.tickTimeout();
                return;
            }

            OtterEntity.this.getLookControl().setLookAt(this.targetPos.x(), this.targetPos.y(), this.targetPos.z(), 85.0F, 85.0F);
            // When the entity goes up it sometimes has a LOT of velocity, so this may help (I hope)
            OtterEntity.this.getNavigation().moveTo(this.targetPos.x(), this.targetPos.y(), this.targetPos.z(), 0.5);

            double dx = this.targetPos.x() - OtterEntity.this.getX();
            double dy = this.targetPos.y() - OtterEntity.this.getEyePosition().y();
            double dz = this.targetPos.z() - OtterEntity.this.getZ();

            double horiz = dx * dx + dz * dz;
            // Pushes the entity to Y+ in case it's near the surface 'line'
            boolean navDone = OtterEntity.this.getNavigation().isDone();
            double basePush = 0.02D;
            if ((dy > 0.0D) && OtterEntity.this.isUnderWater()) {
                if (navDone || horiz <= 0.25D) {
                    Vec3 v = OtterEntity.this.getDeltaMovement();
                    OtterEntity.this.setDeltaMovement(v.x * 0.6D, v.y + 0.01D, v.z * 0.6D);
                }
                else if (horiz <= 9.0D) {
                    OtterEntity.this.push(0.0D, basePush, 0.0D);
                }

            }

            // Hardcoded velocity clamp near the desired surface as high velocity tends to push the otter far away from the
            // relevant pos, thus making it fly in the air
            double absDy = Math.abs(dy);
            if (absDy <= 0.85725D) {
                OtterEntity.this.setDeltaMovement(OtterEntity.this.getDeltaMovement().scale(0.35D));
            }

            // Starts the floating state
            if (absDy <= 0.1D && OtterEntity.this.isReadyToFloat()) {
                OtterEntity.this.setDeltaMovement(OtterEntity.this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
                OtterEntity.this.setYya(0.0F);
                OtterEntity.this.setSpeed(0.0F);
                OtterEntity.this.startFloating(OtterEntity.this.getRandom().nextInt(80, 201));

                this.stop();

                return;
            }

            double dist  = dx * dx + dy * dy + dz * dz;
            if (dist > this.lastDist - 0.0001D) {
                this.stuckTicks++;
            }
            else {
                this.stuckTicks = 0;
            }

            this.lastDist = dist;

            // Fallback if the entity isn't near of the desired pos
            if ((navDone && dist > 2.25D) || this.stuckTicks > 20) {
                if (OtterEntity.this.isReadyToFloat()) {
                    OtterEntity.this.startFloating(OtterEntity.this.getRandom().nextInt(80, 201));
                    this.stop();
                    return;
                }

                searchTargetPos();
                this.tickTimeout();
                this.stuckTicks = 0;
            }

        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        private void tickTimeout() {
            if (this.timeoutTimer % 2 == 0) {
                ((ServerLevel) OtterEntity.this.level()).sendParticles(ParticleTypes.BUBBLE, OtterEntity.this.getRandomX(0.6D), OtterEntity.this.getY(), OtterEntity.this.getRandomZ(0.6D), 2, 0.0D, 0.1D, 0.0D, 0.0D);
            }
            if (this.timeoutTimer <= 0) {
                OtterEntity.this.playSound(CACSounds.OTTER_AMBIENT.get(), OtterEntity.this.getSoundVolume(), 0.3F);
                OtterEntity.this.rejectFood();
                this.stop();
                return;
            }
            --this.timeoutTimer;
        }

        @Override
        public void stop() {
            OtterEntity.this.setNeedsSurface(false);
            OtterEntity.this.getNavigation().stop();
            this.timeoutTimer = this.timeoutTime;

            this.targetPos = null;
            this.stuckTicks = 0;
            this.lastDist = Double.MAX_VALUE;
        }

        private Vec3 findAirPosition() {
            Iterable<BlockPos> blocksInRadius = BlockPos.betweenClosed(Mth.floor(OtterEntity.this.getX() - 1.0D), Mth.floor(OtterEntity.this.getBlockY()), Mth.floor(OtterEntity.this.getZ() - 1.0D), Mth.floor(OtterEntity.this.getX() + 1.0D), Mth.floor(OtterEntity.this.getY() + 32.0D), Mth.floor(OtterEntity.this.getZ() + 1.0D));

            for (BlockPos pos : blocksInRadius) {
                if (OtterEntity.this.level().getBlockState(pos).isAir()) {
                    return Vec3.atBottomCenterOf(pos);
                }
            }

            return null;
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
