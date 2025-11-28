package com.github.eterdelta.crittersandcompanions.entity.brain.control;

import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.block.state.BlockState;

public class OtterMoveControl extends MoveControl {
    private final OtterEntity otter;

    public OtterMoveControl(OtterEntity otterEntity) {
        super(otterEntity);
        this.otter = otterEntity;
    }

    @Override
    public void tick() {
        if (this.otter.isInWater()) {
            if (!this.otter.needsSurface()) {
                this.otter.setDeltaMovement(this.otter.getDeltaMovement().add(this.otter.getLookAngle().scale(this.otter.isFloating() ? 0.002F : 0.005F)));
            }

            if (!this.otter.isFloating()) {
                if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
                    double d0 = this.wantedX - this.mob.getX();
                    double d1 = this.wantedY - this.mob.getY();
                    double d2 = this.wantedZ - this.mob.getZ();
                    double distanceSqr = d0 * d0 + d1 * d1 + d2 * d2;

                    if (distanceSqr < (double) 2.5000003E-7F) {
                        this.mob.setZza(0.0F);
                    } else {
                        float yRot = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                        this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRot, 40.0F));
                        this.mob.yBodyRot = this.mob.getYRot();
                        this.mob.yHeadRot = this.mob.getYRot();
                        float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                        this.mob.setSpeed(speed * 0.2F);

                        double horizontalDistance = Math.sqrt(d0 * d0 + d2 * d2);
                        if (Math.abs(d1) > (double) 1.0E-5F || Math.abs(horizontalDistance) > (double) 1.0E-5F) {
                            float xRot = -((float) (Mth.atan2(d1, horizontalDistance) * (double) (180F / (float) Math.PI)));
                            xRot = Mth.clamp(Mth.wrapDegrees(xRot), -180.0F, 180.0F);

                            // Fallback for the otter going to the abyss of the sea after hunting a fish when it failed to float (although this should not happen)
                            if (this.otter.needsSurface() && xRot > 0.0F) {
                                xRot = 0.0F;
                            }

                            this.mob.setXRot(this.rotlerp(this.mob.getXRot(), xRot, 45.0F));
                        }

                        BlockPos wantedPos = BlockPos.containing(this.wantedX, this.wantedY, this.wantedZ);
                        BlockState wantedBlockState = this.mob.level().getBlockState(wantedPos);

                        if (d1 > 0.6 && d0 * d0 + d2 * d2 < 4.0F && d1 <= 1.0D && wantedBlockState.getFluidState().isEmpty()) {
                            this.mob.getJumpControl().jump();

                            // Decreased speed factor if it's inside water
                            float waterFactor = 0.14F;
                            if (this.otter.needsSurface()) {
                                waterFactor = 0.08F;
                            }

                            this.mob.setSpeed(speed * waterFactor);
                        }

                        float f0 = Mth.cos(this.mob.getXRot() * ((float) Math.PI / 180F));
                        float f1 = Mth.sin(this.mob.getXRot() * ((float) Math.PI / 180F));
                        this.mob.zza = f0 * speed;
                        this.mob.yya = -f1 * (speed);
                    }
                } else {
                    this.mob.setSpeed(0.0F);
                    this.mob.setXxa(0.0F);
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                }
            }
        } else {
            super.tick();
        }
    }
}
