package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.phys.Vec3;

public class FlyingTamablePanicGoal extends TamableAnimal.TamableAnimalPanicGoal {

    public FlyingTamablePanicGoal(TamableAnimal tamable, double speedModifier) {
        tamable.super(speedModifier);
    }

    public FlyingTamablePanicGoal(TamableAnimal tamable, double speedModifier, TagKey<DamageType> panicCausingDamageTypes) {
        tamable.super(speedModifier, panicCausingDamageTypes);
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
