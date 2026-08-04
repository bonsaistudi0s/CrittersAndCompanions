package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

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
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.OtterNavigation;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BabyHealthPenaltyBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviours;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.control.OtterLookControl;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.control.OtterMoveControl;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.EntityUtils;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class OtterEntity extends Animal implements GeoEntity {
    private static final EntityDataAccessor<Boolean> FLOATING = SynchedEntityData.defineId(OtterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(OtterEntity.class, EntityDataSerializers.BOOLEAN);

    private static final RawAnimation SWIM_2_ANIM = RawAnimation.begin().thenLoop("swim_2");
    private static final RawAnimation STANDING_EAT_CLAM_ANIM = RawAnimation.begin().then("standing_eat_clam", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation STANDING_EAT_ANIM = RawAnimation.begin().then("standing_eat", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SWIM_ANIM = RawAnimation.begin().thenLoop("swim");
    private static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation FLOATING_EAT_ANIM = RawAnimation.begin().then("floating_eat", Animation.LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.OTTER.getKey());

    private static final Vec3i UNDERWATER_PICKUP_REACH = new Vec3i(1, 1, 1);

    private boolean needsSurface;
    private int huntDelay;
    private int eatDelay;
    private int floatTime;

    public OtterEntity(EntityType<? extends OtterEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new OtterMoveControl(this);
        this.lookControl = new OtterLookControl(this);
        this.setCanPickUpLoot(true);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        EntityUtils.applyAwarenessMaluses(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    public static boolean checkOtterSpawnRules(EntityType<OtterEntity> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource random) {
        return blockPos.getY() > levelAccessor.getSeaLevel() - 16;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLOATING, false);
        builder.define(EATING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new OtterPanicGoal(this, 1.6F));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 32.0F, 0.9D, 1.5D, (livingEntity -> livingEntity.equals(this.getLastHurtMob()))));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new GoToSurfaceGoal(this, 60));
        this.goalSelector.addGoal(4, new OtterBreedGoal(this));
        this.goalSelector.addGoal(5, new SearchFoodGoal(this));
        this.goalSelector.addGoal(6, new OtterFollowParentGoal(this));
        this.goalSelector.addGoal(7, new OtterRandomStrollGoal(this));
        this.goalSelector.addGoal(8, new OtterLookAtPlayerGoal(this));
        this.goalSelector.addGoal(9, new OtterRandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractFish.class, 20, false, false, (fish) -> fish instanceof AbstractSchoolingFish && this.getHuntDelay() <= 0));
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new BabyHealthPenaltyBehaviour(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("HuntDelay", this.getHuntDelay());
        compound.putBoolean("Floating", this.isFloating());
        compound.putInt("FloatTime", this.floatTime);
        compound.putBoolean("Eating", this.isEating());
        compound.putInt("EatDelay", this.eatDelay);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.huntDelay = compound.getInt("HuntDelay");
        this.setFloating(compound.getBoolean("Floating"));
        this.floatTime = compound.getInt("FloatTime");
        this.setEating(compound.getBoolean("Eating"));
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
    protected int getBaseExperienceReward() {
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

            var held = getMainHandItem();
            if (this.isFood(held)) {
                if (this.isEating()) {
                    if (this.eatDelay > 0) {
                        --this.eatDelay;
                    } else if (level() instanceof ServerLevel level) {
                        breakAndEat(level, held);
                    }
                } else {
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

    private boolean breakingClamOnLand() {
        var floating = isInWater() || isFloating();
        return !floating && getMainHandItem().is(CACItems.CLAM.get());
    }

    private void breakAndEat(ServerLevel level, ItemStack held) {
        Vec3 mouthPos = calculateMouthPos();
        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, held.copy()), mouthPos.x(), mouthPos.y(), mouthPos.z(), 2, 0.0D, 0.1D, 0.0D, 0.05D);
        var sound = getMainHandItem().is(CACItems.CLAM.get()) && !breakingClamOnLand() ?
                CACSounds.OTTER_CLAM_BREAK.get()
                : CACSounds.OTTER_EAT.get();
        playSound(sound, 1.2F, 1.0F);
        eatOrOpen(level, held);
        setEating(false);
    }

    public ItemStack eatOrOpen(Level level, ItemStack itemStack) {
        if (itemStack.is(CACItems.CLAM.get())) {
            if (this.random.nextFloat() <= 0.25F) {
                Vec3 mouthPos = this.calculateMouthPos();
                ItemEntity pearl = new ItemEntity(level, mouthPos.x(), mouthPos.y(), mouthPos.z(), new ItemStack(CACItems.PEARL.get()));

                pearl.setDeltaMovement(this.getRandom().nextGaussian() * 0.05D, this.getRandom().nextGaussian() * 0.05D + 0.2D, this.getRandom().nextGaussian() * 0.05D);
                level.addFreshEntity(pearl);
            }
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.TURTLE_EGG_BREAK, SoundSource.NEUTRAL, 0.8F, 1.5F);
            itemStack.shrink(1);
            return itemStack;
        } else {
            return eat(level, itemStack);
        }
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.6F : 1.0F;
    }

    private static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
        var sourceStack = itemEntity.getItem();
        var removedStack = sourceStack.split(1);
        if (sourceStack.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(sourceStack);
            itemEntity.setPickUpDelay(20);
        }

        return removedStack;
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        if (this.rejectedItem(itemEntity)) {
            return;
        }

        var taken = removeOneItemFromItemEntity(itemEntity);

        var equippedWithStack = this.equipItemIfPossible(taken);
        if (!equippedWithStack.isEmpty()) {
            this.onItemPickup(itemEntity);
            this.take(itemEntity, equippedWithStack.getCount());
        }
    }

    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        if (!getMainHandItem().isEmpty()) {
            return false;
        }

        return super.wantsToPickUp(stack);
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
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double) 0.08F * this.getAttribute(CACAttributes.getSwimSpeed()).getValue(), 0.0D));
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
        return stack.is(TAGS.food());
    }

    @Override
    public boolean canBreed() {
        return !this.isBaby();
    }

    @Override
    public OtterEntity getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
        return CACEntities.OTTER.get().create(level);
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
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

    private RawAnimation animation(AnimationState<?> event) {
        if (isFloating()) {
            return SWIM_2_ANIM;
        }

        if (isEating()) {
            if (getMainHandItem().is(CACItems.CLAM.get())) {
                return STANDING_EAT_CLAM_ANIM;
            }

            return STANDING_EAT_ANIM;
        }

        if (isInWater()) {
            return SWIM_ANIM;
        }

        if (event.isMoving()) {
            if (getDeltaMovement().length() >= 0.18F) {
                return RUN_ANIM;
            } else {
                return WALK_ANIM;
            }
        }

        return IDLE_ANIM;
    }

    private PlayState predicate(AnimationState<?> event) {
        event.getController().setAnimation(animation(event));
        return PlayState.CONTINUE;
    }

    private PlayState floatingHandsPredicate(AnimationState<?> event) {
        if (isFloating() && isEating()) {
            event.getController().setAnimation(FLOATING_EAT_ANIM);
            return PlayState.CONTINUE;
        }
        event.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
        controllers.add(new AnimationController<>(this, "floating_hands_controller", 4, this::floatingHandsPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public boolean isHungryAt(ItemStack foodStack) {
        return foodStack.is(CACItems.CLAM.get()) || this.getInLoveTime() <= 0;
    }

    public void rejectFood() {
        if (!this.getMainHandItem().isEmpty()) {
            ItemStack thrownAway = this.getMainHandItem().copy();
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), thrownAway);
            itemEntity.setPickUpDelay(40);
            itemEntity.setThrower(this);
            this.getMainHandItem().shrink(thrownAway.getCount());
            this.level().addFreshEntity(itemEntity);
        }
    }

    public boolean rejectedItem(ItemEntity itemEntity) {
        if (itemEntity.getOwner() != null) {
            return itemEntity.getOwner().getUUID().equals(this.getUUID());
        }
        return false;
    }

    private void startEating() {
        if (this.isFood(this.getMainHandItem())) {
            this.eatDelay = this.getMainHandItem().is(CACItems.CLAM.get()) ? 45 : 12;
            this.setEating(true);
            if (breakingClamOnLand()) {
                playSound(CACSounds.OTTER_CLAM_BREAK_LAND.get(), 1.2F, 1.0F);
            }
        }
    }

    public void startFloating(int time) {
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

}
