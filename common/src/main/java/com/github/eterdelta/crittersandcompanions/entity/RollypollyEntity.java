package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.entity.animation.BugAnimations;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.Behaviours;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.DancingBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.TameableBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.VariantBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.goal.DancingStrollGoal;
import com.github.eterdelta.crittersandcompanions.entity.brain.goal.TameablePanicGoal;
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
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RollypollyEntity extends TamableAnimal implements GeoEntity {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(RollypollyEntity.class, EntityDataSerializers.INT);
    public static final AnimalTags TAGS = AnimalTags.create(CACEntities.ROLLYPOLLY.getKey());

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RollypollyEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new VariantBehaviour(this, VARIANT, 7));
        behaviours.add(new DancingBehaviour(this));
        behaviours.add(new TameableBehaviour(this, TAGS));
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new TameablePanicGoal(this, 1.25D));
        goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(2, TAGS.temptGoal(this));
        goalSelector.addGoal(6, new BreedGoal(this, 1.25D));
        goalSelector.addGoal(7, new FollowOwnerGoal(this, 1.4D, 10F, 2F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        goalSelector.addGoal(11, new DancingStrollGoal<>(this, 1.0D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 12.0D).add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(TAGS.food());
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob entity) {
        return CACEntities.ROLLYPOLLY.get().create(level);
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
