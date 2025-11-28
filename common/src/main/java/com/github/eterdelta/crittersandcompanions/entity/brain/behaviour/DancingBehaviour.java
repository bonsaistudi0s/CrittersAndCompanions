package com.github.eterdelta.crittersandcompanions.entity.brain.behaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class DancingBehaviour implements Behaviour {

    private final Entity owner;

    private boolean dancing = false;
    @Nullable
    private BlockPos activeJukebox = null;

    public DancingBehaviour(Entity owner) {
        this.owner = owner;
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean active) {
        activeJukebox = pos;
        dancing = active;
    }

    private boolean proceed() {
        if (activeJukebox == null) return false;
        return activeJukebox.closerToCenterThan(owner.position(), 5.0D) || !owner.level().getBlockState(activeJukebox).is(Blocks.JUKEBOX);
    }

    @Override
    public void aiStep() {
        if (!proceed()) {
            activeJukebox = null;
            dancing = false;
        }
    }

    public boolean isDancing() {
        return dancing;
    }

}
