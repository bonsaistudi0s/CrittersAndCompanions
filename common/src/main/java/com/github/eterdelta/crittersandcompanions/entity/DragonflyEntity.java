package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.Behaviours;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.TameableBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.control.DragonflyMoveControl;
import com.github.eterdelta.crittersandcompanions.item.DragonflyArmorItem;
import com.github.eterdelta.crittersandcompanions.registry.AnimalTags;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
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
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DragonflyEntity extends TamableAnimal implements GeoEntity {

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.DRAGONFLY.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DragonflyEntity(EntityType<? extends DragonflyEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new DragonflyMoveControl(this);

        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
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
        this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 6.0F, 2.0F));
        this.goalSelector.addGoal(3, TAGS.temptGoal(this));
        this.goalSelector.addGoal(4, new RandomFlyGoal());

        this.targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
    }

    @Override
    public int getBaseExperienceReward() {
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
    public boolean canBeLeashed() {
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
                stack.consume(1, player);
                playSound(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 0.4F, 1.5F);

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
    public void setTame(boolean tame, boolean sideEffects) {
        super.setTame(tame, sideEffects);
        var tameModifier = CrittersAndCompanions.createId("tamed_health_boost");
        if (tame && sideEffects) {
            getAttribute(Attributes.MAX_HEALTH)
                    .addOrReplacePermanentModifier(new AttributeModifier(tameModifier, 4.0, AttributeModifier.Operation.ADD_VALUE));
        } else {
            getAttribute(Attributes.MAX_HEALTH).removeModifier(tameModifier);
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
        return getBodyArmorItem();
    }

    public void setArmor(ItemStack armorItem) {
        setBodyArmorItem(armorItem);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
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
}
