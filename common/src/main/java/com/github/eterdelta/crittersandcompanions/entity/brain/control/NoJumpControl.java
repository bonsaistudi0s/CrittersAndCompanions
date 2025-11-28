package com.github.eterdelta.crittersandcompanions.entity.brain.control;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.JumpControl;

public class NoJumpControl extends JumpControl {

    public NoJumpControl(PathfinderMob mob) {
        super(mob);
    }

    @Override
    public void jump() {
    }
}
