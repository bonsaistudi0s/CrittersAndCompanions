package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.entity.animation.BugAnimations;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.Behaviours;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.DancingBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.TameableBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.VariantBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.goal.DancingStrollGoal;
import com.github.eterdelta.crittersandcompanions.registry.AnimalTags;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WeevilEntity extends TamableAnimal implements GeoEntity {

    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.WEEVIL.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WeevilEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new DancingBehaviour(this));
        behaviours.add(new TameableBehaviour(this, TAGS));
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new DancingStrollGoal<>(this, 1.0D));
        goalSelector.addGoal(2, TAGS.temptGoal(this));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.2D);
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

}
