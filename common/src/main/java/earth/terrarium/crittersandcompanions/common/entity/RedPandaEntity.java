package earth.terrarium.crittersandcompanions.common.entity;

import earth.terrarium.crittersandcompanions.common.handler.AnimalHandler;
import earth.terrarium.crittersandcompanions.common.registry.CACEntities;
import earth.terrarium.crittersandcompanions.common.registry.CACSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class RedPandaEntity extends TamableAnimal implements GeoEntity {
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
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private LivingEntity alerter;

    public RedPandaEntity(EntityType<? extends RedPandaEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new RedPandaMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 18.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SLEEPING, false);
        this.entityData.define(ALERT, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new AlertGoal());
        this.goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new SleepGoal(140));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.25D));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.0D, Ingredient.of(Items.SWEET_BERRIES), false));
        this.goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(8, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
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
    public int getExperienceReward() {
        return this.random.nextInt(2, 5);
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.6F : 1.0F;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
        RedPandaEntity redPanda = CACEntities.RED_PANDA.get().create(level);
        return redPanda;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        if (!this.isSleeping()) {
            ItemStack handStack = player.getItemInHand(interactionHand);

            if (!this.isTame()) {
                if (handStack.is(Items.SWEET_BERRIES)) {
                    if (!player.getAbilities().instabuild) {
                        handStack.shrink(1);
                    }
                    if (!this.level().isClientSide()) {
                        if (this.random.nextInt(10) == 0 && !AnimalHandler.onAnimalTame(this, player)) {
                            this.tame(player);
                            this.level().broadcastEntityEvent(this, (byte) 7);
                        } else {
                            this.level().broadcastEntityEvent(this, (byte) 6);
                        }
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }
            } else if (this.isTame() && this.isOwnedBy(player)) {
                if (!this.isFood(handStack) && !handStack.is(Items.SWEET_BERRIES)) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                } else if (this.getHealth() < this.getMaxHealth()) {
                    this.gameEvent(GameEvent.EAT, this);
                    this.heal(2.0F);
                    if (!player.getAbilities().instabuild) {
                        handStack.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }
            }
            return super.mobInteract(player, interactionHand);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.BAMBOO);
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData, CompoundTag p_146750_) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, p_146750_);
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
        if (this.isAlert()) {
            event.getController().setAnimation(RawAnimation.begin().then("red_panda_angry", Animation.LoopType.PLAY_ONCE));
        } else if (this.isInSittingPose()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("red_panda_sit"));
        } else if (this.isSleeping()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("red_panda_sleeping"));
        } else if (event.isMoving()) {
            if (this.getSpeed() >= 0.8F) {
                // TODO double check this, this used to be animation speed
                event.getController().setAnimation(RawAnimation.begin().thenLoop("red_panda_run"));
            } else {
                event.getController().setAnimation(RawAnimation.begin().thenLoop("red_panda_walk"));
            }
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("red_panda_idle"));
        }
        return PlayState.CONTINUE;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
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

    static class RedPandaMoveControl extends MoveControl {
        private final RedPandaEntity redPanda;

        public RedPandaMoveControl(RedPandaEntity redPandaEntity) {
            super(redPandaEntity);
            this.redPanda = redPandaEntity;
        }

        @Override
        public void tick() {
            if (!this.redPanda.isSleeping()) {
                super.tick();
            }
        }
    }

    public class SleepGoal extends Goal {
        private final int countdownTime;
        private int countdown;

        public SleepGoal(int countdownTime) {
            this.countdownTime = countdownTime;
            this.countdown = RedPandaEntity.this.random.nextInt(reducedTickDelay(countdownTime));
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        public boolean canUse() {
            if (!RedPandaEntity.this.isTame() && RedPandaEntity.this.xxa == 0.0F && RedPandaEntity.this.yya == 0.0F && RedPandaEntity.this.zza == 0.0F) {
                return this.canSleep() || RedPandaEntity.this.isSleeping();
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return this.canSleep();
        }

        private boolean canSleep() {
            if (this.countdown > 0) {
                --this.countdown;
                return false;
            } else {
                return RedPandaEntity.this.level().isDay();
            }
        }

        public void stop() {
            RedPandaEntity.this.setSleeping(false);
            this.countdown = RedPandaEntity.this.random.nextInt(this.countdownTime);
        }

        public void start() {
            RedPandaEntity.this.setJumping(false);
            RedPandaEntity.this.setSleeping(true);
            RedPandaEntity.this.getNavigation().stop();
            RedPandaEntity.this.getMoveControl().setWantedPosition(RedPandaEntity.this.getX(), RedPandaEntity.this.getY(), RedPandaEntity.this.getZ(), 0.0D);
        }
    }

    public class AlertGoal extends Goal {
        private int time;

        public AlertGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.time = reducedTickDelay(40);
        }

        @Override
        public boolean canUse() {
            if (!RedPandaEntity.this.isSleeping()) {
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
            this.time = 20;
            RedPandaEntity.this.setAlert(false);
        }
    }
}
