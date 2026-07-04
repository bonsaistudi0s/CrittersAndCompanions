package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class WallClimberMoveControl extends MoveControl {

    public WallClimberMoveControl(PathfinderMob mob) {
        super(mob);
    }

    @Override
    public void tick() {
        if (operation == Operation.MOVE_TO && !mob.getNavigation().isDone()) {
            double d0 = wantedX - mob.getX();
            double d2 = wantedZ - mob.getZ();
            float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;

            mob.setYRot(rotlerp(mob.getYRot(), f, 90.0F));
            mob.yBodyRot = mob.getYRot();

            var speed = mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            speed *= getSpeedModifier();
            mob.setSpeed((float) speed);
        } else {
            mob.setSpeed(0.0F);
        }
    }

}
