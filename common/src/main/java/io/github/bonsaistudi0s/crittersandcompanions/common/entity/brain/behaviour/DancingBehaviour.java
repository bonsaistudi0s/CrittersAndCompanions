package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

public class DancingBehaviour implements Behaviour {

    private final Entity owner;
    private final int variants;

    private boolean dancing = false;
    private int currentDanceIndex = 1;
    @Nullable
    private BlockPos activeJukebox = null;

    public DancingBehaviour(Entity owner) {
        this(owner, 1);
    }

    public DancingBehaviour(Entity owner, int variants) {
        this.owner = owner;
        this.variants = variants;
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean active) {
        activeJukebox = pos;
        dancing = active;
        if (active && variants > 1) {
            currentDanceIndex = owner.getRandom().nextInt(variants) + 1;
        }
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

    public String getDanceAnimationName() {
        return currentDanceIndex == 1 ? "dance" : "dance_" + currentDanceIndex;
    }

}
