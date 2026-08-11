package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.control;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.JumpingSpiderEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class JumpingSpiderMoveControl extends MoveControl {

    private final JumpingSpiderEntity spider;

    public JumpingSpiderMoveControl(JumpingSpiderEntity jumpingSpider) {
        super(jumpingSpider);
        this.spider = jumpingSpider;
    }

    public void tick() {
        if (this.hasWanted() && this.spider.onGround() && this.spider.getRandom().nextFloat() <= 0.025F) {
            var dx = this.wantedX - this.spider.getX();
            var dz = this.wantedZ - this.spider.getZ();
            var jumpDir = new Vec3(dx, 0.0, dz).normalize();
            this.spider.setDeltaMovement(
                    this.spider.getDeltaMovement().add(
                            jumpDir.x, 0.5, jumpDir.z
                    )
            );
            this.spider.getNavigation().stop();
        }

        super.tick();
    }
}
