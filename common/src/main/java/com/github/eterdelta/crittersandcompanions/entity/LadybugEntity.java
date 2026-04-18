package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.entity.animation.BugAnimations;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.Behaviours;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.DancingBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.TameableBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.goal.FlyingTameablePanicGoal;
import com.github.eterdelta.crittersandcompanions.registry.AnimalTags;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LadybugEntity extends TamableAnimal implements GeoEntity, FlyingAnimal {

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.LADYBUG.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public LadybugEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new DancingBehaviour(this));
        behaviours.add(new TameableBehaviour(this, TAGS));
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FlyingTameablePanicGoal(this, 1.25D));
        goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(2, TAGS.temptGoal(this));
        goalSelector.addGoal(6, new BreedGoal(this, 1.25D));
        goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.4D, 10F, 2F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        goalSelector.addGoal(11, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FLYING_SPEED, 0.4D);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(TAGS.food());
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob entity) {
        return null;
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
    protected PathNavigation createNavigation(Level level) {
        var navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        var motion = getDeltaMovement();
        if (isFlying() && motion.y < 0) {
            setDeltaMovement(motion.multiply(1.0F, 0.6, 1.0F));
        }
    }

    public boolean isFlying() {
        return !onGround();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
        this.resetFallDistance();
    }

    @Override
    protected boolean canFlyToOwner() {
        return true;
    }
}
