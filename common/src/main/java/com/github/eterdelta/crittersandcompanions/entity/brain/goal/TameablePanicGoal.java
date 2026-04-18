package com.github.eterdelta.crittersandcompanions.entity.brain.goal;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class TameablePanicGoal extends PanicGoal {

    private final TamableAnimal animal;

    public TameablePanicGoal(TamableAnimal animal, double speedModifier) {
        super(animal, speedModifier);
        this.animal = animal;
    }

    @Override
    public boolean canUse() {
        if (animal.isTame()) {
            return false;
        }
        return super.canUse();
    }
}

