package com.github.eterdelta.crittersandcompanions.entity.brain.control;

import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.world.entity.ai.control.LookControl;

public class OtterLookControl extends LookControl {
    private final OtterEntity otter;

    public OtterLookControl(OtterEntity otterEntity) {
        super(otterEntity);
        this.otter = otterEntity;
    }

    @Override
    public void tick() {
        if (this.otter.isInWater()) {
            if (this.lookAtCooldown > 0) {
                --this.lookAtCooldown;
                this.getYRotD().ifPresent((p_181134_) -> {
                    this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, p_181134_ + 20.0F, this.yMaxRotSpeed);
                });
                this.getXRotD().ifPresent((p_181132_) -> {
                    this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), p_181132_ + 10.0F, this.xMaxRotAngle));
                });
            } else {
                if (this.mob.getNavigation().isDone()) {
                    this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), 0.0F, 5.0F));
                }

                this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
            }
        } else {
            super.tick();
        }
    }
}
