package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.animation.BugAnimations;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.BehaviourDriven;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.Behaviours;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.DancingBehaviour;
import com.github.eterdelta.crittersandcompanions.entity.brain.DancingStrollGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LadybugEntity extends TamableAnimal implements GeoEntity, BehaviourDriven {

    private static final TagKey<Item> TAME_TAG = TagKey.create(Registries.ITEM, CrittersAndCompanions.createId("ladybug_tempt_items"));
    private static final TagKey<Item> FOODS_TAG = TagKey.create(Registries.ITEM, CrittersAndCompanions.createId("ladybug_food"));

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Behaviours behaviours = new Behaviours()
            .add(new DancingBehaviour(this));

    @Override
    public Behaviours getBehaviours() {
        return behaviours;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new DancingStrollGoal<>(this, 1.0D));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public LadybugEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(FOODS_TAG);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob entity) {
        return null;
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean active) {
        behaviours.the(DancingBehaviour.class).setRecordPlayingNearby(pos, active);
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
