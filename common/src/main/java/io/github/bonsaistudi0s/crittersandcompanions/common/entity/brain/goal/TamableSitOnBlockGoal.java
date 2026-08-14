package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TamableSitOnBlockGoal extends MoveToBlockGoal {
    private final TamableAnimal mob;

    public TamableSitOnBlockGoal(TamableAnimal mob, double speedModifier) {
        super(mob, speedModifier, 8);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return this.mob.isTame() && !this.mob.isOrderedToSit() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.mob.setInSittingPose(false);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setInSittingPose(false);
    }

    @Override
    public void tick() {
        super.tick();
        
        BlockPos currentPos = this.mob.blockPosition();
        boolean onBlock = this.isValidTarget(this.mob.level(), currentPos) || this.isValidTarget(this.mob.level(), currentPos.below());
        
        if (onBlock) {
            this.mob.getNavigation().stop();
            this.mob.setInSittingPose(true);
        } else {
            this.mob.setInSittingPose(this.isReachedTarget());
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        if (!level.isEmptyBlock(pos.above())) {
            return false;
        } else {
            BlockState blockState = level.getBlockState(pos);
            if (blockState.is(Blocks.CHEST)) {
                return ChestBlockEntity.getOpenCount(level, pos) < 1;
            } else {
                return blockState.is(Blocks.FURNACE) && blockState.getValue(FurnaceBlock.LIT) || blockState.is(BlockTags.BEDS);
            }
        }
    }
}
