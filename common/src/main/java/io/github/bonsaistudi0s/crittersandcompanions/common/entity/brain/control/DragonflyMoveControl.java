package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.control;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.DragonflyEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;

public class DragonflyMoveControl extends FlyingMoveControl {
    public DragonflyMoveControl(DragonflyEntity dragonfly) {
        super(dragonfly, 360, true);
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            this.mob.setNoGravity(true);
            double deltaX = this.wantedX - this.mob.getX();
            double deltaY = this.wantedY - this.mob.getY();
            double deltaZ = this.wantedZ - this.mob.getZ();
            double distanceSqrt = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
            if (distanceSqrt < 0.1) {
                this.mob.setYya(mob.getRandom().nextFloat() - 0.5F);
                this.mob.setZza(0.0F);
                return;
            }

            float f = (float) (Mth.atan2(deltaZ, deltaX) * (180F / (float) Math.PI)) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 360.0F));

            float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));

            this.mob.setSpeed(speed);
            double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
            if (Math.abs(deltaY) > 1.0E-5F || Math.abs(horizontalDistance) > 1.0E-5F) {
                float f2 = (float) (-(Mth.atan2(deltaY, horizontalDistance) * (180F / (float) Math.PI)));
                this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f2, 20.0F));
                this.mob.setYya(deltaY > 0.0D ? speed : -speed);
            }
        } else {
            this.mob.setYya(mob.getRandom().nextFloat() - 0.5F);
            this.mob.setZza(0.0F);
        }
    }
}
