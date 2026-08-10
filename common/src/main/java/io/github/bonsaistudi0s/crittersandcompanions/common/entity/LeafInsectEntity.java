package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.animation.BugAnimations;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BabyHealthPenaltyBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviours;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.DancingBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.VariantBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.DancingStrollGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.LeafInsectSearchLeavesGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACSounds;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.EntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class LeafInsectEntity extends Animal implements GeoEntity {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(LeafInsectEntity.class, EntityDataSerializers.INT);

    private static final int TRANSITION_TICK_TIME = 4;

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.LEAF_INSECT.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int barterTime;
    private int barterCooldown;

    public LeafInsectEntity(EntityType<? extends LeafInsectEntity> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);
        EntityUtils.applyAwarenessMaluses(this);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new VariantBehaviour(this, VARIANT, 3));
        behaviours.add(new DancingBehaviour(this));
        behaviours.add(new BabyHealthPenaltyBehaviour(this));
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        goalSelector.addGoal(3, new LeafInsectSearchLeavesGoal(this));
        goalSelector.addGoal(4, TAGS.temptGoal(this));
        goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
        goalSelector.addGoal(6, new DancingStrollGoal<>(this, 1.0D));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0D).add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    public static boolean checkLeafInsectSpawnRules(EntityType<LeafInsectEntity> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource random) {
        BlockState blockState = levelAccessor.getBlockState(blockPos.below());
        return blockPos.getY() > levelAccessor.getSeaLevel() - 16 && (blockState.is(BlockTags.DIRT) || blockState.is(BlockTags.LEAVES));
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CACSounds.LEAF_INSECT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CACSounds.LEAF_INSECT_DEATH.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(LeafInsectAnimations.createController(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public @Nullable LeafInsectEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        var baby = CACEntities.LEAF_INSECT.get().create(level);
        if (baby == null) {
            return null;
        }

        if (otherParent instanceof LeafInsectEntity otherLeafInsectParent) {
            baby.behaviour(VariantBehaviour.class).inherit(this, otherLeafInsectParent);
        }

        return baby;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(TAGS.food());
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !hasCustomName();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("BarterTime", this.barterTime);
        compound.putInt("BarterCooldown", this.barterCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.barterTime = compound.getInt("BarterTime");
        this.barterCooldown = compound.getInt("BarterCooldown");
    }

    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        if (!this.getMainHandItem().isEmpty() || this.barterCooldown > 0) {
            return false;
        }
        return stack.is(ItemTags.LEAVES);
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        var sourceStack = itemEntity.getItem();
        var taken = sourceStack.split(1);

        var equippedWithStack = this.equipItemIfPossible(taken);
        if (!equippedWithStack.isEmpty()) {
            this.onItemPickup(itemEntity);
            this.take(itemEntity, equippedWithStack.getCount());
            this.barterTime = 10;
            if (!level().isClientSide) {
                this.triggerAnim("controller", "eat");
            }
        }

        if (sourceStack.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(sourceStack);
            itemEntity.setPickUpDelay(20);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isAlive() && this.isControlledByLocalInstance()) {
            if (this.barterCooldown > 0) {
                this.barterCooldown--;
            }

            if (this.barterTime > 0) {
                this.barterTime--;
                var handStack = this.getMainHandItem();

                if (this.barterTime % 3 == 0 && !handStack.isEmpty()) {
                    this.playSound(CACSounds.OTTER_EAT.get(), 0.5F + 0.5F * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    if (this.level() instanceof ServerLevel serverLevel && handStack.getItem() instanceof BlockItem blockItem) {
                        var mouthPos = new Vec3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ()).add(this.getViewVector(1.0F).scale(0.2));
                        serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockItem.getBlock().defaultBlockState()), mouthPos.x, mouthPos.y, mouthPos.z, 3, 0.05D, 0.05D, 0.05D, 0.05D);
                    }
                }

                if (this.barterTime == 0) {
                    if (!handStack.isEmpty() && handStack.getItem() instanceof BlockItem blockItem) {
                        if (this.level() instanceof ServerLevel serverLevel) {
                            var lootTable = serverLevel.getServer().getLootData().getLootTable(blockItem.getBlock().getLootTable());
                            var lootParams = new LootParams.Builder(serverLevel)
                                    .withParameter(LootContextParams.ORIGIN, this.position())
                                    .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                                    .withParameter(LootContextParams.BLOCK_STATE, blockItem.getBlock().defaultBlockState())
                                    .create(LootContextParamSets.BLOCK);

                            List<ItemStack> drops = List.of();
                            var tries = 0;
                            while (drops.isEmpty() && tries < 50) {
                                drops = lootTable.getRandomItems(lootParams);
                                tries++;
                            }

                            if (drops.isEmpty()) {
                                drops = List.of(new ItemStack(Items.STICK));
                            }

                            for (var drop : drops) {
                                var dropEntity = new ItemEntity(serverLevel, this.getX(), this.getY() + 0.5D, this.getZ(), drop);
                                dropEntity.setDeltaMovement(this.getRandom().nextGaussian() * 0.05D, this.getRandom().nextGaussian() * 0.05D + 0.2D, this.getRandom().nextGaussian() * 0.05D);
                                serverLevel.addFreshEntity(dropEntity);
                            }

                            this.playSound(SoundEvents.ITEM_PICKUP, 0.5F, 1.2F);
                            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                            this.barterCooldown = 20;
                        }
                    }
                }
            }
        }
    }

    public float lookBlendWeight = 1.0f;
    public float lookBlendWeightO = 1.0f;

    private boolean shouldUseAnimationHeadRotation() {
        var controller = this.getAnimatableInstanceCache()
                .getManagerForId(this.getId())
                .getAnimationControllers()
                .get("controller");

        var isPlayingEatAnim = controller != null
                && controller.getCurrentAnimation() != null
                && controller.getCurrentAnimation().animation().name().equals("eat");

        return isPlayingEatAnim && controller.getAnimationState() == AnimationController.State.RUNNING;
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            return;
        }

        this.lookBlendWeightO = this.lookBlendWeight;

        var targetWeight = shouldUseAnimationHeadRotation() ? 0.0f : 1.0f;
        this.lookBlendWeight = Mth.lerp(1f / TRANSITION_TICK_TIME, this.lookBlendWeight, targetWeight);
    }

    private static class LeafInsectAnimations extends BugAnimations<LeafInsectEntity> {

        private static final RawAnimation EAT_ANIM = RawAnimation.begin().thenPlay("eat");

        public LeafInsectAnimations(Behaviours behaviours) {
            super(behaviours);
        }

        public static AnimationController<LeafInsectEntity> createController(LeafInsectEntity animatable) {
            return new AnimationController<>(animatable, "controller", TRANSITION_TICK_TIME, new LeafInsectEntity.LeafInsectAnimations(animatable.getBehaviours()))
                    .triggerableAnim("eat", EAT_ANIM);
        }
    }
}
