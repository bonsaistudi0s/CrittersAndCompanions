package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.animation.BugAnimations;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.DancingStrollGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.FreezeWhileChestAccessedGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal.TameablePanicGoal;
import io.github.bonsaistudi0s.crittersandcompanions.common.menu.RolyPolyMenu;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACSounds;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.EntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RolyPolyEntity extends TamableAnimal implements GeoEntity {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(RolyPolyEntity.class,
            EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_CHEST = SynchedEntityData.defineId(RolyPolyEntity.class,
            EntityDataSerializers.BOOLEAN);
    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.ROLY_POLY.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RolyPolyEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        EntityUtils.applyAwarenessMaluses(this);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new ChestBehaviour(this, HAS_CHEST, RolyPolyMenu.SLOTS,
                (id, inv, container) -> new RolyPolyMenu(id, inv, container,
                        () -> getBehaviours().the(ChestBehaviour.class).dropEquipment())));
        behaviours.add(new VariantBehaviour(this, VARIANT, 7));
        behaviours.add(new DancingBehaviour(this, 2));
        behaviours.add(new TameableBehaviour(this, TAGS));
        behaviours.add(new HealthRegenerationBehaviour(this));
        behaviours.add(new BabyHealthPenaltyBehaviour(this));
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new FreezeWhileChestAccessedGoal(this));
        goalSelector.addGoal(2, new TameablePanicGoal(this, 1.25D));
        goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(4, new BreedGoal(this, 1.25D));
        goalSelector.addGoal(5, TAGS.temptGoal(this));
        goalSelector.addGoal(6, new FollowParentGoal(this, 1.25D));
        goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.4D, 10F, 2F, false));
        goalSelector.addGoal(8, new DancingStrollGoal<>(this, 1.0D));
        goalSelector.addGoal(9, TAGS.sittingTemptGoal(this));
        goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(11, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(TAGS.food());
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Override
    public RolyPolyEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        var baby = CACEntities.ROLY_POLY.get().create(level);
        if (baby == null) {
            return null;
        }

        if (otherParent instanceof RolyPolyEntity otherRolyPolyParent) {
            baby.behaviour(VariantBehaviour.class).inherit(this, otherRolyPolyParent);
        }

        if (isTame()) {
            baby.setOwnerUUID(getOwnerUUID());
            baby.setTame(true);
        }

        return baby;
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
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return CACSounds.BUGS_HURT.get();
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        if (HAS_CHEST.equals(key)) {
            this.refreshDimensions();
        }
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pose) {
        var baseDimensions = super.getDimensions(pose);

        if (getBehaviours().the(ChestBehaviour.class).hasChest()) {
            return EntityDimensions.fixed(baseDimensions.width, baseDimensions.height + 0.5F);
        }

        return baseDimensions;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !isTame() && !hasCustomName();
    }
}
