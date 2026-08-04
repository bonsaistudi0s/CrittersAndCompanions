package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BabyHealthPenaltyBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviours;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.TameableBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.VariantBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.FerretDigGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.FerretSleepGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.SprintingFollowOwnerGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.TameableFollowParentGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACSounds;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.EntityUtils;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FerretEntity extends TamableAnimal implements GeoEntity {

    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DIGGING = SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.INT);

    private static final int TRANSITION_TICK_TIME = 4;
    private static final RawAnimation DIG_ANIM = RawAnimation.begin().then("dig", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SIT_ANIM = RawAnimation.begin().thenLoop("sit");
    private static final RawAnimation SLEEP_ANIM = RawAnimation.begin().thenLoop("sleep");
    private static final RawAnimation SWIM_ANIM = RawAnimation.begin().thenLoop("swim");
    private static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.FERRET.getKey());
    public static final TagKey<Block> DIG_GROUNDS_TAG = TagKey.create(Registries.BLOCK, CrittersAndCompanions.createId("ferret_dig_grounds"));

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected BlockState stateToDig;
    protected int digCooldown;

    public FerretEntity(EntityType<? extends FerretEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FerretMoveControl();
        EntityUtils.applyAwarenessMaluses(this);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new VariantBehaviour(this, VARIANT, 2));
        behaviours.add(new TameableBehaviour(this, TAGS));
        behaviours.add(new BabyHealthPenaltyBehaviour(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.34D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLEEPING, false);
        builder.define(DIGGING, false);
        builder.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
    }

    @Nullable
    public BlockState getDiggingState() {
        return stateToDig;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        goalSelector.addGoal(2, new FerretDigGoal(this));
        goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(4, new FerretSleepGoal(this, 200));
        goalSelector.addGoal(5, new AvoidEntityGoal<>(this, LivingEntity.class, 8.0F, 1.6D, 1.4D, (livingEntity) -> livingEntity.is(this.getLastHurtByMob()) && !livingEntity.is(this.getOwner())));
        goalSelector.addGoal(6, new BreedGoal(this, 1.25D));
        goalSelector.addGoal(7, new MeleeAttackGoal(this, 1.5D, true));
        goalSelector.addGoal(7, new SprintingFollowOwnerGoal(this, 1.4D, 10.0F, 5.0F, 2.0F));
        goalSelector.addGoal(8, TAGS.temptGoal(this));
        goalSelector.addGoal(10, new TameableFollowParentGoal(this, 1.0D));
        goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(12, TAGS.sittingTemptGoal(this));
        goalSelector.addGoal(13, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(14, new RandomLookAroundGoal(this));

        targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Animal.class, 10, false, false, (entity) -> entity instanceof Chicken || entity instanceof Rabbit));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Sleeping", isSleeping());
        if (getCollarColor() != null) {
            compound.putInt("CollarColor", getCollarColor().getId());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSleeping(compound.getBoolean("Sleeping"));
        if (compound.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }
    }

    @Override
    public int getBaseExperienceReward() {
        return this.random.nextInt(2, 5);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.digCooldown > 0) {
            this.digCooldown--;
        }
    }

    @Override
    public FerretEntity getBreedOffspring(ServerLevel level, AgeableMob other) {
        var baby = CACEntities.FERRET.get().create(level);
        if (baby == null) return null;

        UUID uuid = this.getOwnerUUID();
        if (other instanceof FerretEntity otherFerret) {
            baby.behaviour(VariantBehaviour.class).inherit(this, otherFerret);

            var color = random.nextBoolean() ? getCollarColor() : otherFerret.getCollarColor();
            if (color != null) baby.setCollarColor(color);

            if (uuid != null) {
                baby.setOwnerUUID(uuid);
                baby.setTame(true, false);
            }
        }
        return baby;
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {

        if (isTame() && isOwnedBy(player) && !isSleeping()) {
            var stack = player.getItemInHand(hand);

            var digResult = startDigging(player, stack);
            if (digResult != InteractionResult.PASS) return digResult;

            if (stack.getItem() instanceof DyeItem dyeItem && getCollarColor() != dyeItem.getDyeColor()) {
                setCollarColor(dyeItem.getDyeColor());
                stack.consume(1, player);
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    private InteractionResult startDigging(Player player, ItemStack handStack) {
        if (handStack.is(TAGS.tempt()) && !isBaby() && !isInSittingPose()) {
            if (digCooldown <= 0) {
                stateToDig = level().getBlockState(blockPosition().below());

                if (stateToDig.is(DIG_GROUNDS_TAG)) {
                    setDigging(true);
                    digCooldown = 6000;
                    handStack.consume(1, player);
                    return InteractionResult.sidedSuccess(level().isClientSide());
                } else {
                    stateToDig = null;
                }
            }

            return InteractionResult.FAIL;
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean canFallInLove() {
        return !this.isDigging() && super.canFallInLove();
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(TAGS.food());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? null : CACSounds.FERRET_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CACSounds.FERRET_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CACSounds.FERRET_DEATH.get();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
        if (mobSpawnType.equals(MobSpawnType.SPAWNER) && this.random.nextFloat() <= 0.2F) {
            for (int i = 0; i < this.random.nextInt(1, 4); i++) {
                var baby = CACEntities.FERRET.get().create(this.level());
                baby.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
                baby.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                baby.setBaby(true);
                levelAccessor.addFreshEntity(baby);
            }
        }
        return spawnGroupData;
    }

    private PlayState predicate(AnimationState<?> event) {
        var controller = event.getController();

        if (this.isDigging()) {
            controller.transitionLength(TRANSITION_TICK_TIME);
            controller.setAnimation(DIG_ANIM);
        } else if (this.isInSittingPose()) {
            controller.transitionLength(0);
            controller.setAnimation(SIT_ANIM);
        } else if (this.isSleeping()) {
            controller.transitionLength(TRANSITION_TICK_TIME);
            controller.setAnimation(SLEEP_ANIM);
        } else if (isInWater()) {
            controller.transitionLength(TRANSITION_TICK_TIME);
            controller.setAnimation(SWIM_ANIM);
        } else if (event.isMoving()) {
            controller.transitionLength(TRANSITION_TICK_TIME);
            controller.setAnimation(RUN_ANIM);
        } else {
            if (event.isCurrentAnimation(SIT_ANIM)) {
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

    public boolean isDigging() {
        return this.entityData.get(DIGGING);
    }

    public void setDigging(boolean digging) {
        this.entityData.set(DIGGING, digging);
        if (!digging) {
            stateToDig = null;
        }
    }

    @Nullable
    public DyeColor getCollarColor() {
        if (!isTame()) return null;
        return DyeColor.byId(entityData.get(DATA_COLLAR_COLOR));
    }

    private void setCollarColor(DyeColor color) {
        entityData.set(DATA_COLLAR_COLOR, color.getId());
    }

    class FerretMoveControl extends MoveControl {
        public FerretMoveControl() {
            super(FerretEntity.this);
        }

        public void tick() {
            if (!mob.isSleeping()) {
                super.tick();
            }
        }
    }
}
