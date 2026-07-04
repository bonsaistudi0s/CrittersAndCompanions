package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.phys.Vec3;

public class FlyingTameablePanicGoal extends TameablePanicGoal {
    public FlyingTameablePanicGoal(TamableAnimal animal, double speedModifier) {
        super(animal, speedModifier);
    }

    @Override
    protected boolean findRandomPosition() {
        Vec3 viewVector = this.mob.getViewVector(0.0F);
        Vec3 target = AirRandomPos.getPosTowards(this.mob, 16, 7, 7, viewVector, (float) (Math.PI / 2));
        if (target == null) {
            return false;
        }

        this.posX = target.x;
        this.posY = target.y;
        this.posZ = target.z;
        return true;
    }
}
