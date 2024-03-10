package earth.terrarium.crittersandcompanions.common.block;

import earth.terrarium.crittersandcompanions.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SeaBunnySlimeBlock extends HalfTransparentBlock {

    public SeaBunnySlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float p_154571_) {
    }

    public boolean isStickyBlock(BlockState state) {
        return true;
    }

    public boolean canStickTo(BlockState state, BlockState other) {
        //Todo, fix with mixin
        if (state.is(ModBlocks.SEA_BUNNY_SLIME_BLOCK.get()) && (other.is(Blocks.SLIME_BLOCK) || other.is(Blocks.HONEY_BLOCK))) {
            return false;
        }
        return state.is(Blocks.SLIME_BLOCK) || state.is(Blocks.HONEY_BLOCK);
    }
}
