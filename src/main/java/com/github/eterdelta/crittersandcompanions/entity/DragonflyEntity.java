package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.item.DragonflyArmorItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
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

public class DragonflyEntity extends TamableAnimal implements GeoEntity {
    private static final EntityDataAccessor<ItemStack> ARMOR_ITEM = SynchedEntityData.defineId(DragonflyEntity.class, EntityDataSerializers.ITEM_STACK);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DragonflyEntity(EntityType<? extends DragonflyEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new DragonflyMoveControl(this);

        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.FLYING_SPEED, 0.25D);
    }

    public static boolean checkDragonflySpawnRules(EntityType<DragonflyEntity> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource random) {
        int seaLevel = levelAccessor.getSeaLevel();
        return blockPos.getY() > seaLevel - 10 && blockPos.getY() <= seaLevel + 16 && levelAccessor.getBlockState(blockPos).isAir() && levelAccessor.getRawBrightness(blockPos, 0) > 8;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ARMOR_ITEM, ItemStack.EMPTY);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 6.0F, 2.0F, true));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(Items.SPIDER_EYE), false));
        this.goalSelector.addGoal(4, new RandomFlyGoal());

        this.targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("ArmorItem", this.getArmor().save(new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setArmor(ItemStack.of(compound.getCompound("ArmorItem")));
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
        ItemStack handStack = player.getItemInHand(hand);

        if (this.isTame()) {
            if (handStack.is(Items.SPIDER_EYE) && this.getHealth() < this.getMaxHealth()) {
                this.gameEvent(GameEvent.EAT, this);
                this.heal(2.0F);
                if (!player.getAbilities().instabuild) {
                    handStack.shrink(1);
                }
            } else if (this.isOwnedBy(player)) {
                if (!this.level().isClientSide()) {
                    if (handStack.getItem() instanceof DragonflyArmorItem armorItem && this.getArmor().isEmpty()) {
                        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(armorItem.getHealthBuff());
                        this.setHealth(armorItem.getHealthBuff());
                        this.setArmor(handStack.copy());
                        handStack.shrink(1);
                        if (!player.getAbilities().instabuild) {
                            handStack.shrink(1);
                        }
                        this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 0.4F, 1.5F);

                    } else if (player.isCrouching() && !this.getArmor().isEmpty()) {
                        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
                        this.setHealth(8.0F);
                        this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getArmor().copy()));
                        this.setArmor(ItemStack.EMPTY);
                        this.playSound(SoundEvents.ITEM_PICKUP, 0.2F, 1.0F);

                    } else {
                        this.setOrderedToSit(!this.isOrderedToSit());
                    }
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
        } else {
            if (handStack.is(Items.SPIDER_EYE)) {
                if (!player.getAbilities().instabuild) {
                    handStack.shrink(1);
                }
                if (!this.level().isClientSide()) {
                    if (this.random.nextInt(10) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                        this.tame(player);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void setTame(boolean tame) {
        super.setTame(tame);
        if (tame) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
            this.setHealth(8.0F);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(4.0D);
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
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
        return this.entityData.get(ARMOR_ITEM);
    }

    public void setArmor(ItemStack armorItem) {
        this.entityData.set(ARMOR_ITEM, armorItem);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    static class DragonflyMoveControl extends FlyingMoveControl {
        public DragonflyMoveControl(DragonflyEntity dragonfly) {
            super(dragonfly, 360, true);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                this.operation = Operation.WAIT;
                this.mob.setNoGravity(true);
                double deltaX = this.wantedX - this.mob.getX();
                double deltaY = this.wantedY - this.mob.getY();
                double deltaZ = this.wantedZ - this.mob.getZ();
                double distanceSqrt = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
                if (distanceSqrt < 0.1) {
                    this.mob.setYya(mob.getRandom().nextFloat() - 0.5F);
                    this.mob.setZza(0.0F);
                    return;
                }

                float f = (float) (Mth.atan2(deltaZ, deltaX) * (180F / (float) Math.PI)) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 360.0F));

                float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));

                this.mob.setSpeed(speed);
                double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                if (Math.abs(deltaY) > 1.0E-5F || Math.abs(horizontalDistance) > 1.0E-5F) {
                    float f2 = (float) (-(Mth.atan2(deltaY, horizontalDistance) * (180F / (float) Math.PI)));
                    this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f2, 20.0F));
                    this.mob.setYya(deltaY > 0.0D ? speed : -speed);
                }
            } else {
                this.mob.setYya(mob.getRandom().nextFloat() - 0.5F);
                this.mob.setZza(0.0F);
            }
        }
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
            //Vec3 randomPos = RandomPos.generateRandomPos(DragonflyEntity.this, () ->
            //        new BlockPos(
            //                DragonflyEntity.this.getBlockX() + DragonflyEntity.this.random.nextIntBetweenInclusive(-horizontalRange, horizontalRange),
            //                DragonflyEntity.this.getBlockY() + DragonflyEntity.this.random.nextInt(verticalRange) - 2,
            //                DragonflyEntity.this.getBlockZ() + DragonflyEntity.this.random.nextIntBetweenInclusive(-horizontalRange, horizontalRange)
            //        )
            //);
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
