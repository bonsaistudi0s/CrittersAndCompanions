package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BabyHealthPenaltyBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviours;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.TameableBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.SprintingFollowOwnerGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.TamableLieOnBedGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.TamableRelaxOnOwnerGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.TamableSitOnBlockGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACSounds;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.EntityUtils;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RedPandaEntity extends TamableAnimal implements GeoEntity {

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.RED_PANDA.getKey());

    protected static final List<EntityType<? extends Mob>> SCAREABLES = new ArrayList<>(Arrays.asList(
            EntityType.BEE,
            EntityType.ENDERMAN,
            EntityType.IRON_GOLEM,
            EntityType.LLAMA,
            EntityType.POLAR_BEAR,
            EntityType.SPIDER,
            EntityType.CAVE_SPIDER,
            EntityType.VEX,
            EntityType.WOLF,
            EntityType.ZOMBIFIED_PIGLIN
    ));
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ALERT = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int TRANSITION_TICK_TIME = 3;
    private static final RawAnimation ANGRY_ANIM = RawAnimation.begin().thenPlay("angry");
    private static final RawAnimation SIT_ANIM = RawAnimation.begin().thenLoop("sit");
    private static final RawAnimation SLEEP_ANIM = RawAnimation.begin().thenLoop("sleep");
    private static final RawAnimation SWIM_ANIM = RawAnimation.begin().thenLoop("swim");
    private static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private LivingEntity alerter;

    public RedPandaEntity(EntityType<? extends RedPandaEntity> entityType, Level level) {
        super(entityType, level);
        EntityUtils.applyAwarenessMaluses(this);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new TameableBehaviour(this, TAGS));
        behaviours.add(new BabyHealthPenaltyBehaviour(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 18.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLEEPING, false);
        builder.define(ALERT, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TamableAnimal.TamableAnimalPanicGoal(1.4D));
        this.goalSelector.addGoal(2, new AlertGoal());
        this.goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new TamableRelaxOnOwnerGoal<>(this, RedPandaEntity::isSleeping, this::setSleeping));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.25D));
        this.goalSelector.addGoal(6, TAGS.temptGoal(this));
        this.goalSelector.addGoal(7, new TamableLieOnBedGoal<>(this, 1.1D, 8, RedPandaEntity::isSleeping, this::setSleeping));
        this.goalSelector.addGoal(8, new SprintingFollowOwnerGoal(this, 1.25D, 10.0F, 5.0F, 2.0F));
        this.goalSelector.addGoal(9, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new TamableSitOnBlockGoal(this, 0.8D));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(12, TAGS.sittingTemptGoal(this));
        this.goalSelector.addGoal(13, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(14, new RandomLookAroundGoal(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Sleeping", this.isSleeping());
        compound.putBoolean("Alert", this.isAlert());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.setAlert(compound.getBoolean("Alert"));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isSleeping()) {
            this.setXRot(0.0F);
            this.yHeadRot = this.yBodyRot;
            this.yHeadRotO = this.yBodyRotO;
        }
    }

    @Override
    public int getBaseExperienceReward() {
        return random.nextInt(2, 5);
    }

    @Override
    public float getScale() {
        return isBaby() ? 0.6F : 1.0F;
    }

    @Override
    public RedPandaEntity getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
        return CACEntities.RED_PANDA.get().create(level);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(TAGS.food());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? null : CACSounds.RED_PANDA_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CACSounds.RED_PANDA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CACSounds.RED_PANDA_DEATH.get();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
        if (mobSpawnType.equals(MobSpawnType.SPAWNER) && ((AgeableMobGroupData) spawnGroupData).getGroupSize() >= 2 && this.random.nextFloat() <= 0.4F) {
            for (int i = 0; i < this.random.nextInt(1, 3); i++) {
                RedPandaEntity baby = CACEntities.RED_PANDA.get().create(this.level());
                baby.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                baby.setBaby(true);
                levelAccessor.addFreshEntity(baby);
            }
        }
        return spawnGroupData;
    }

    private PlayState predicate(AnimationState<?> event) {
        var controller = event.getController();

        if (this.isAlert()) {
            controller.transitionLength(TRANSITION_TICK_TIME);
            controller.setAnimation(ANGRY_ANIM);
        } else if (this.isInSittingPose()) {
            controller.transitionLength(0);
            controller.setAnimation(SIT_ANIM);
        } else if (this.isSleeping()) {
            controller.transitionLength(0);
            controller.setAnimation(SLEEP_ANIM);
        } else if (isInWater()) {
            controller.transitionLength(TRANSITION_TICK_TIME);
            controller.setAnimation(SWIM_ANIM);
        } else if (event.isMoving()) {
            controller.transitionLength(TRANSITION_TICK_TIME);

            if (getDeltaMovement().length() >= 0.16F) {
                controller.setAnimation(RUN_ANIM);
            } else {
                controller.setAnimation(WALK_ANIM);
            }
        } else {
            if (event.isCurrentAnimation(SIT_ANIM) || event.isCurrentAnimation(SLEEP_ANIM)) {
                controller.transitionLength(0);
            } else {
                controller.transitionLength(TRANSITION_TICK_TIME);
            }

            controller.setAnimation(IDLE_ANIM);
        }

        return PlayState.CONTINUE;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", TRANSITION_TICK_TIME, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    public boolean isAlert() {
        return this.entityData.get(ALERT);
    }

    protected void setAlert(boolean alert) {
        this.entityData.set(ALERT, alert);
    }

    public class AlertGoal extends Goal {
        private int time;

        public AlertGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.time = reducedTickDelay(25);
        }

        @Override
        public boolean canUse() {
            if (!RedPandaEntity.this.isSleeping() && !RedPandaEntity.this.isInWater()) {
                List<LivingEntity> nearAlerters = RedPandaEntity.this.level().getEntitiesOfClass(LivingEntity.class, RedPandaEntity.this.getBoundingBox().inflate(4.0D),
                        (livingEntity) -> RedPandaEntity.this.isTame() ? SCAREABLES.contains(livingEntity.getType()) && ((Mob) livingEntity).isAggressive() : livingEntity instanceof Player);
                LivingEntity nearestAlerter = RedPandaEntity.this.level().getNearestEntity(nearAlerters, TargetingConditions.forNonCombat().range(4.0D), RedPandaEntity.this, RedPandaEntity.this.getX(), RedPandaEntity.this.getY(), RedPandaEntity.this.getZ());

                if (nearestAlerter != RedPandaEntity.this.alerter) {
                    RedPandaEntity.this.alerter = nearestAlerter;
                    return RedPandaEntity.this.alerter != null;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.time > 0;
        }

        @Override
        public void start() {
            this.time = reducedTickDelay(25);
            RedPandaEntity.this.setAlert(true);
            RedPandaEntity.this.getNavigation().stop();
            RedPandaEntity.this.getMoveControl().setWantedPosition(RedPandaEntity.this.getX(), RedPandaEntity.this.getY(), RedPandaEntity.this.getZ(), 0.0D);
        }

        @Override
        public void tick() {
            RedPandaEntity.this.getLookControl().setLookAt(RedPandaEntity.this.alerter);
            --this.time;
        }

        @Override
        public void stop() {
            this.time = 25;
            RedPandaEntity.this.setAlert(false);
        }
    }
}
