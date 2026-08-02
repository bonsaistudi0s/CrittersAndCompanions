package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.animation.BugAnimations;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BabyHealthPenaltyBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviours;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.DancingBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.VariantBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.DancingStrollGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LeafInsectEntity extends Animal implements GeoEntity {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(LeafInsectEntity.class, EntityDataSerializers.INT);

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.LEAF_INSECT.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public LeafInsectEntity(EntityType<? extends LeafInsectEntity> entityType, Level level) {
        super(entityType, level);
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
        goalSelector.addGoal(3, TAGS.temptGoal(this));
        goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        goalSelector.addGoal(5, new DancingStrollGoal<>(this, 1.0D));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
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
        controllers.add(BugAnimations.createController(this));
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
}
