package com.github.eterdelta.crittersandcompanions.entity.brain;

import com.github.eterdelta.crittersandcompanions.entity.brain.goal.DancingStrollGoal;
import com.github.eterdelta.crittersandcompanions.registry.AnimalTags;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;

public class CompanionAI {

    public static void addTargetSelectors(TamableAnimal mob, GoalSelector targetSelector) {
        targetSelector.addGoal(1, new OwnerHurtByTargetGoal(mob));
        targetSelector.addGoal(2, new OwnerHurtTargetGoal(mob));
        targetSelector.addGoal(3, new HurtByTargetGoal(mob).setAlertOthers());
    }

    public static void addGoalSelectors(TamableAnimal mob, AnimalTags tags, GoalSelector goalSelector) {
        goalSelector.addGoal(3, new SitWhenOrderedToGoal(mob));
        goalSelector.addGoal(2, tags.temptGoal(mob));
        goalSelector.addGoal(5, new MeleeAttackGoal(mob, 1.0F, true));
        goalSelector.addGoal(6, new BreedGoal(mob, 1.25D));
        goalSelector.addGoal(7, new FollowOwnerGoal(mob, 1.4D, 10F, 2F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(mob));
        goalSelector.addGoal(11, new DancingStrollGoal<>(mob, 1.0D));
    }

}
