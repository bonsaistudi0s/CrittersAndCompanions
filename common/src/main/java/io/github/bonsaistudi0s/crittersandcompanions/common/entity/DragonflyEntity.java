package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviours;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.TameableBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.control.DragonflyMoveControl;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.FlyingAvoidEntityGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.FlyingTamablePanicGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.item.DragonflyArmorItem;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.EntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.UUID;

public class DragonflyEntity extends TamableAnimal implements GeoEntity {

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.DRAGONFLY.getKey());

    private static final UUID TAME_MODIFIER_UUID = UUID.fromString("b028af68-a321-4e9d-b5f3-55064b895754");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DragonflyEntity(EntityType<? extends DragonflyEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new DragonflyMoveControl(this);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
        EntityUtils.applyAwarenessMaluses(this);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new TameableBehaviour(this, TAGS));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.FLYING_SPEED, 0.25D);
    }

    public static boolean checkDragonflySpawnRules(EntityType<DragonflyEntity> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource random) {
        int seaLevel = levelAccessor.getSeaLevel();
        return blockPos.getY() > seaLevel - 10 && blockPos.getY() <= seaLevel + 16 && levelAccessor.getBlockState(blockPos).isAir() && levelAccessor.getRawBrightness(blockPos, 0) > 8;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FlyingTamablePanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new FlyingAvoidJumpingSpidersGoal(this, 8.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 6.0F, 2.0F, true));
        this.goalSelector.addGoal(6, TAGS.temptGoal(this));
        this.goalSelector.addGoal(7, new RandomFlyGoal());
        this.goalSelector.addGoal(8, TAGS.sittingTemptGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public int getExperienceReward() {
        return this.random.nextInt(2, 5);
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos) {
        return !this.isTame() && this.level().getBiome(blockPos).is(Biomes.RIVER) ? 10.0F : 5.0F;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isOrderedToSit()) {
            this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.16D, 0.0D));
        }
    }

    @Override
    public void travel(Vec3 speed) {
        if (this.isEffectiveAi()) {
            this.moveRelative(this.getSpeed(), speed);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
        }
        this.calculateEntityAnimation(false);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
        flyingPathNavigation.setCanFloat(true);
        return flyingPathNavigation;
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        return false;
    }

    @Override
    protected void checkFallDamage(double p_20990_, boolean p_20991_, BlockState p_20992_, BlockPos p_20993_) {
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        if (isTame() && isOwnedBy(player)) {
            var success = InteractionResult.sidedSuccess(level().isClientSide());

            if (stack.getItem() instanceof DragonflyArmorItem && getArmor().isEmpty()) {
                setArmor(stack.copy());
                stack.shrink(1);
                playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 0.4F, 1.5F);

                return success;
            }

            if (stack.isEmpty() && player.isCrouching() && !getArmor().isEmpty()) {
                level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getArmor().copy()));
                setArmor(ItemStack.EMPTY);
                playSound(SoundEvents.ITEM_PICKUP, 0.2F, 1.0F);

                return success;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);

        var maxHealthAttr = this.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthAttr != null) {
            if (tamed) {
                maxHealthAttr.removeModifier(TAME_MODIFIER_UUID);

                var modifier = new AttributeModifier(
                        TAME_MODIFIER_UUID,
                        "Tamed health bonus",
                        4.0,
                        AttributeModifier.Operation.ADDITION
                );

                maxHealthAttr.addPermanentModifier(modifier);
            } else {
                maxHealthAttr.removeModifier(TAME_MODIFIER_UUID);
            }
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(TAGS.food());
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    private PlayState predicate(AnimationState<?> event) {
        if (this.isInSittingPose()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("dragonfly_sit"));
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("dragonfly_fly"));
        }
        return PlayState.CONTINUE;
    }

    public ItemStack getArmor() {
        return getItemBySlot(EquipmentSlot.CHEST);
    }

    public void setArmor(ItemStack armorItem) {
        setItemSlot(EquipmentSlot.CHEST, armorItem);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !isTame() && !hasCustomName();
    }

    public class RandomFlyGoal extends Goal {
        private static final int horizontalRange = 14;
        private static final int verticalRange = 4;

        public RandomFlyGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return !DragonflyEntity.this.isOrderedToSit() && DragonflyEntity.this.navigation.isDone() && DragonflyEntity.this.random.nextInt(10) == 0;
        }

        @Override
        public void start() {
            var view = DragonflyEntity.this.getViewVector(0.0F);
            var randomPos = HoverRandomPos.getPos(DragonflyEntity.this, horizontalRange, verticalRange, view.x, view.z, 2, 3, 1);
            if (randomPos == null) {
                var y = DragonflyEntity.this.isInWater() ? 2 : -2;
                randomPos = AirAndWaterRandomPos.getPos(DragonflyEntity.this, horizontalRange, verticalRange, y, view.x, view.y, 2);
            }
            if (randomPos != null) {
                DragonflyEntity.this.navigation.moveTo(DragonflyEntity.this.navigation.createPath(BlockPos.containing(randomPos), 1), 1.0);
            }
        }
    }

    private static class FlyingAvoidJumpingSpidersGoal extends FlyingAvoidEntityGoal<JumpingSpiderEntity> {

        public FlyingAvoidJumpingSpidersGoal(PathfinderMob mob, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier) {
            super(mob, JumpingSpiderEntity.class, maxDistance, walkSpeedModifier, sprintSpeedModifier);
        }

        private boolean shouldAvoid() {
            if (this.toAvoid == null) {
                return false;
            }

            var self = (DragonflyEntity) this.mob;
            var bothOwnedBySamePlayer = this.toAvoid.isTame() && self.isTame() && this.toAvoid.getOwnerUUID() == self.getOwnerUUID();
            return !bothOwnedBySamePlayer;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && shouldAvoid();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && shouldAvoid();
        }
    }
}
