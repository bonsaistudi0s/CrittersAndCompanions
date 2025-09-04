package com.github.eterdelta.crittersandcompanions.entity.brain;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;

public class TameableFollowParentGoal extends FollowParentGoal {

    private final TamableAnimal animal;

    public TameableFollowParentGoal(TamableAnimal animal, double speedModifier) {
        super(animal, speedModifier);
        this.animal = animal;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !animal.isOrderedToSit();
    }
}
