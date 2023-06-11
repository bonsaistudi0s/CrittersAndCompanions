package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.capability.CACCapabilities;
import com.github.eterdelta.crittersandcompanions.capability.IBubbleStateCapability;
import com.github.eterdelta.crittersandcompanions.network.CACPacketHandler;
import com.github.eterdelta.crittersandcompanions.network.ClientboundBubbleStatePacket;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.EnumSet;

public class DumboOctopusEntity extends WaterAnimal implements IAnimatable, Bucketable {
    private static final EntityDataAccessor<Boolean> RESTING = SynchedEntityData.defineId(DumboOctopusEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(DumboOctopusEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(DumboOctopusEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public int restTimer;
    protected boolean bubblingPlayer;
    protected ServerPlayer bubbledPlayer;

    public DumboOctopusEntity(EntityType<? extends DumboOctopusEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new DumboOctopusMoveControl(90, 90, 1.0F, 1.0F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 180);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.MOVEMENT_SPEED, 0.06D);
    }

    public static boolean checkDumboOctopusSpawnRules(EntityType<DumboOctopusEntity> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource random) {
        return WaterAnimal.checkSurfaceWaterAnimalSpawnRules(entityType, levelAccessor, spawnType, blockPos, random);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RESTING, false);
        this.entityData.define(VARIANT, 0);
        this.entityData.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new DumboOctopusEntity.BubblePlayerGoal());
        this.goalSelector.addGoal(1, new DumboOctopusEntity.RandomSwimmingGoal(this, 1.0D, 40));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Resting", this.isResting());
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setResting(compound.getBoolean("Resting"));
        this.setVariant(compound.getInt("Variant"));
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(ItemStack bucketStack) {
        CompoundTag bucketCompound = bucketStack.getOrCreateTag();
        Bucketable.saveDefaultDataToBucketTag(this, bucketStack);
        bucketCompound.putInt("BucketVariant", this.getVariant());
    }

    @Override
    public void loadFromBucketTag(CompoundTag bucketCompound) {
        Bucketable.loadDefaultDataFromBucketTag(this, bucketCompound);
        if (bucketCompound.contains("BucketVariant")) {
            this.setVariant(bucketCompound.getInt("BucketVariant"));
        }
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(CACItems.DUMBO_OCTOPUS_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_AXOLOTL;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return entityDimensions.height * 0.5F;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level.isClientSide() && !this.bubblingPlayer && this.isEffectiveAi()) {
            if (this.isInWater()) {
                if (this.isResting()) {
                    if (--this.restTimer <= 0) {
                        this.setResting(false);
                    }
                    this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.01D, 0.0D));
                } else if (this.random.nextFloat() <= 0.001F) {
                    this.restTimer = this.random.nextInt(200, 601);
                    this.setResting(true);
                }
            } else {
                this.setResting(false);
            }
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public void travel(Vec3 speed) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), speed);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(speed);
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        return Bucketable.bucketMobPickup(player, interactionHand, this).orElse(super.mobInteract(player, interactionHand));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData, CompoundTag p_146750_) {
        this.setVariant(this.random.nextInt(0, 4));
        return spawnGroupData;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isResting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("dumbo_octopus_idle", ILoopType.EDefaultLoopTypes.LOOP));
        } else if (this.isInWater()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("dumbo_octopus_swim", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("dumbo_octopus_on_land", ILoopType.EDefaultLoopTypes.LOOP));
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

    public void sendBubble(ServerPlayer player, boolean state) {
        LazyOptional<IBubbleStateCapability> capability = player.getCapability(CACCapabilities.BUBBLE_STATE);

        capability.ifPresent(bubbleState -> {
            bubbleState.setActive(state);
            CACPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                    new ClientboundBubbleStatePacket(state, player.getId()));
        });
    }

    public ServerPlayer getBubbledPlayer() {
        return this.bubbledPlayer;
    }

    public boolean isResting() {
        return this.entityData.get(RESTING);
    }

    public void setResting(boolean resting) {
        this.entityData.set(RESTING, resting);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Mth.clamp(variant, 0, 3));
    }

    static class RandomSwimmingGoal extends net.minecraft.world.entity.ai.goal.RandomSwimmingGoal {
        private final DumboOctopusEntity dumboOctopus;

        public RandomSwimmingGoal(DumboOctopusEntity dumboOctopus, double speedModifier, int interval) {
            super(dumboOctopus, speedModifier, interval);
            this.dumboOctopus = dumboOctopus;
        }

        @Override
        public boolean canUse() {
            return !this.dumboOctopus.isResting() && super.canUse();
        }
    }

    class BubblePlayerGoal extends Goal {
        private final Level level;
        private final PathNavigation navigation;
        private int timeToRecalcPath;
        private boolean bubbleSent;

        public BubblePlayerGoal() {
            this.level = DumboOctopusEntity.this.level;
            this.navigation = DumboOctopusEntity.this.getNavigation();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            ServerPlayer player = (ServerPlayer) level.getNearestPlayer(DumboOctopusEntity.this, 6.0D);
            if (player != null && player.getAirSupply() < 150) {
                DumboOctopusEntity.this.bubbledPlayer = player;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return DumboOctopusEntity.this.bubbledPlayer != null && DumboOctopusEntity.this.bubbledPlayer.isAlive() && DumboOctopusEntity.this.bubbledPlayer.getAirSupply() <= 150;
        }

        @Override
        public void start() {
            DumboOctopusEntity.this.bubblingPlayer = true;
            this.timeToRecalcPath = 0;
        }

        @Override
        public void tick() {
            DumboOctopusEntity.this.getLookControl().setLookAt(DumboOctopusEntity.this.bubbledPlayer, 10.0F, (float) DumboOctopusEntity.this.getMaxHeadXRot());

            if (DumboOctopusEntity.this.distanceToSqr(DumboOctopusEntity.this.bubbledPlayer) > 2.0D) {
                this.timeToRecalcPath--;

                if (this.timeToRecalcPath <= 0) {
                    this.timeToRecalcPath = this.adjustedTickDelay(10);
                    if (!DumboOctopusEntity.this.isLeashed() && !DumboOctopusEntity.this.isPassenger()) {
                        this.navigation.moveTo(DumboOctopusEntity.this.bubbledPlayer, 2.0D);
                    }
                }
            } else {
                if (!this.bubbleSent) {
                    DumboOctopusEntity.this.sendBubble(DumboOctopusEntity.this.bubbledPlayer, true);
                    this.bubbleSent = true;
                }
            }

            if (this.bubbleSent) {
                DumboOctopusEntity.this.bubbledPlayer.setAirSupply(150);
            }
        }

        @Override
        public void stop() {
            DumboOctopusEntity.this.bubblingPlayer = false;
            DumboOctopusEntity.this.sendBubble(DumboOctopusEntity.this.bubbledPlayer, false);
            this.bubbleSent = false;
            DumboOctopusEntity.this.bubbledPlayer.playSound(CACSounds.BUBBLE_POP.get());
            DumboOctopusEntity.this.bubbledPlayer = null;
            this.navigation.stop();
        }
    }

    class DumboOctopusMoveControl extends SmoothSwimmingMoveControl {
        public DumboOctopusMoveControl(int maxTurnX, int maxTurnY, float inWaterSpeedModifier, float outsideWaterSpeedModifier, boolean applyGravity) {
            super(DumboOctopusEntity.this, maxTurnX, maxTurnY, inWaterSpeedModifier, outsideWaterSpeedModifier, applyGravity);
        }

        @Override
        public void tick() {
            if (!DumboOctopusEntity.this.isResting()) {
                super.tick();
            }
        }
    }
}
