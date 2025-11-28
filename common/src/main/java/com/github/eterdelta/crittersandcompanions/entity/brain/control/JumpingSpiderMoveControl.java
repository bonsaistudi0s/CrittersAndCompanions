package com.github.eterdelta.crittersandcompanions.entity.brain.control;

import com.github.eterdelta.crittersandcompanions.entity.JumpingSpiderEntity;
import net.minecraft.world.entity.ai.control.MoveControl;

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
