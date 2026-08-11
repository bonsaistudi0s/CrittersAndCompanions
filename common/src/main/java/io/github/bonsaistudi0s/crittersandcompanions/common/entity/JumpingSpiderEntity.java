package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.control.JumpingSpiderMoveControl;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.DancingStrollGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.JumpingSpiderLeapGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.TamableAnimalPanicGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.EntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class JumpingSpiderEntity extends TamableAnimal implements GeoEntity {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(JumpingSpiderEntity.class,
            EntityDataSerializers.INT);
    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.JUMPING_SPIDER.getKey());

    private static final int TRANSITION_TICK_TIME = 4;
    private static final RawAnimation DANCE_ANIM = RawAnimation.begin().thenLoop("dance");
    private static final RawAnimation SIT_ANIM = RawAnimation.begin().thenLoop("sit");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private DancingBehaviour dancingBehaviour;

    public JumpingSpiderEntity(EntityType<? extends JumpingSpiderEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new JumpingSpiderMoveControl(this);
        EntityUtils.applyAwarenessMaluses(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Spider.createAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.ATTACK_DAMAGE, 8.0D);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        dancingBehaviour = new DancingBehaviour(this);
        behaviours.add(dancingBehaviour);
        behaviours.add(new TameableBehaviour(this, TAGS));
        behaviours.add(new VariantBehaviour(this, VARIANT, 8));
        behaviours.add(new BabyHealthPenaltyBehaviour(this));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TamableAnimalPanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new JumpingSpiderLeapGoal(this, 0.4F, 0.8F));
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, TAGS.temptGoal(this));
        this.goalSelector.addGoal(8, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, false));
        this.goalSelector.addGoal(9, new DancingStrollGoal<>(this, 0.8D));
        this.goalSelector.addGoal(10, TAGS.sittingTemptGoal(this));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Endermite.class, false, (LivingEntity::isAlive)));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Silverfish.class, false, (LivingEntity::isAlive)));
        this.targetSelector.addGoal(2, new NonTameRandomTargetGoal<>(this, DragonflyEntity.class, false, (LivingEntity::isAlive)));
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (isBaby()) {
            super.setTarget(null);
            return;
        }

        super.setTarget(target);
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.1F, 2.0F);
    }

    @Override
    public void makeStuckInBlock(BlockState blockState, Vec3 p_33797_) {
        if (!blockState.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(blockState, p_33797_);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(TAGS.food());
    }

    @Override
    public @Nullable JumpingSpiderEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        var baby = CACEntities.JUMPING_SPIDER.get().create(level);
        if (baby == null) {
            return null;
        }

        if (otherParent instanceof JumpingSpiderEntity otherJumpingSpiderParent) {
            baby.behaviour(VariantBehaviour.class).inherit(this, otherJumpingSpiderParent);
        }

        if (isTame()) {
            baby.setOwnerUUID(getOwnerUUID());
            baby.setTame(true);
        }

        return baby;
    }

    private PlayState predicate(AnimationState<?> event) {
        var controller = event.getController();

        if (dancingBehaviour.isDancing()) {
            controller.transitionLength(TRANSITION_TICK_TIME);
            controller.setAnimation(DANCE_ANIM);
        } else if (this.isInSittingPose()) {
            controller.transitionLength(0);
            controller.setAnimation(SIT_ANIM);
        } else if (event.isMoving()) {
            controller.transitionLength(TRANSITION_TICK_TIME);
            controller.setAnimation(WALK_ANIM);
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
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller", TRANSITION_TICK_TIME, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return effect.getEffect() != MobEffects.POISON && super.canBeAffected(effect);
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (target instanceof Creeper || target instanceof Ghast || target instanceof ArmorStand) {
            return false;
        }

        if (target instanceof TamableAnimal tamable) {
            return !tamable.isTame() || tamable.getOwner() != owner;
        }

        return super.wantsToAttack(target, owner);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !isTame() && !hasCustomName();
    }
}
