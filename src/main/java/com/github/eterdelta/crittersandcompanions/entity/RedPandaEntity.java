package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.registry.CaCEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;

public class RedPandaEntity extends Animal implements IAnimatable {
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ALERT = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimationFactory factory = new AnimationFactory(this);
    private Player alerter;

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
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
        this.goalSelector.addGoal(2, new RedPandaEntity.AlertGoal());
        this.goalSelector.addGoal(3, new RedPandaEntity.SleepGoal(140));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
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
    protected int getExperienceReward(Player player) {
        return this.random.nextInt(2, 5);
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.6F : 1.0F;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
        RedPandaEntity redPanda = CaCEntities.RED_PANDA.get().create(level);
        return redPanda;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        return this.isSleeping() ? InteractionResult.PASS : super.mobInteract(player, interactionHand);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.BAMBOO);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData, CompoundTag p_146750_) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, p_146750_);
        if (mobSpawnType.equals(MobSpawnType.SPAWNER) && ((AgeableMobGroupData) spawnGroupData).getGroupSize() >= 2 && this.random.nextFloat() <= 0.4F) {
            for (int i = 0; i < this.random.nextInt(1, 3); i++) {
                RedPandaEntity baby = CaCEntities.RED_PANDA.get().create(this.level);
                baby.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                baby.setBaby(true);
                levelAccessor.addFreshEntity(baby);
            }
        }
        return spawnGroupData;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSleeping()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("red_panda_sleeping", true));
        } else if (this.isAlert()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("red_panda_angry", false));
        } else if (event.isMoving()) {
            if (this.animationSpeed >= 0.8F) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("red_panda_run", true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("red_panda_walk", true));
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("red_panda_idle", true));
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
            if (!this.redPanda.isSleeping() && !this.redPanda.isAlert()) {
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
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        }

        public boolean canUse() {
            if (RedPandaEntity.this.xxa == 0.0F && RedPandaEntity.this.yya == 0.0F && RedPandaEntity.this.zza == 0.0F) {
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
                return RedPandaEntity.this.level.isDay();
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
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            this.time = reducedTickDelay(40);
        }

        @Override
        public boolean canUse() {
            if (!RedPandaEntity.this.isSleeping()) {
                Player nearestPlayer = RedPandaEntity.this.level.getNearestPlayer(RedPandaEntity.this.getX(), RedPandaEntity.this.getY(), RedPandaEntity.this.getZ(), 2.0D, true);

                if (nearestPlayer != RedPandaEntity.this.alerter) {
                    RedPandaEntity.this.alerter = nearestPlayer;
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
        }

        @Override
        public void tick() {
            RedPandaEntity.this.getLookControl().setLookAt(RedPandaEntity.this.alerter);
            --this.time;
        }

        @Override
        public void stop() {
            this.time = reducedTickDelay(40);
            RedPandaEntity.this.setAlert(false);
        }
    }
}
