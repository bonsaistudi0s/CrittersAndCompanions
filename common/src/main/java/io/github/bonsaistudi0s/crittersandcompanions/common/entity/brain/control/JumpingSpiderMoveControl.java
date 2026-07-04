package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.control;

import net.minecraft.world.entity.ai.control.MoveControl;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.JumpingSpiderEntity;

public class JumpingSpiderMoveControl extends MoveControl {
    private final JumpingSpiderEntity spider;

    public JumpingSpiderMoveControl(JumpingSpiderEntity jumpingSpider) {
        super(jumpingSpider);
        this.spider = jumpingSpider;
    }

    public void tick() {
        if (this.hasWanted() && this.spider.onGround() && this.spider.getRandom().nextFloat() <= 0.05F) {
            this.spider.setDeltaMovement(this.spider.getDeltaMovement().add(0.0D, 0.6D, 0.0D));
        }
        super.tick();
    }
}
