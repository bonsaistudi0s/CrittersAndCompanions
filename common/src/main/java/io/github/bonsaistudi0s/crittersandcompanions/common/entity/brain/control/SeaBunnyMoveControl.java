package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.control;

import net.minecraft.world.entity.PathfinderMob;

public class SeaBunnyMoveControl extends WallClimberMoveControl {

    public SeaBunnyMoveControl(PathfinderMob mob) {
        super(mob);
    }

    @Override
    public double getSpeedModifier() {
        var base = super.getSpeedModifier();
        if (mob.isInWater()) base += 2.0;
        return base;
    }
}
