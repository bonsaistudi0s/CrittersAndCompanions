package earth.terrarium.crittersandcompanions.common.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.block.SeaBunnySlimeBlock;
import earth.terrarium.crittersandcompanions.blocks.SilkCocoonBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class CACBlocks {
    public static final ResourcefulRegistry<Block> BLOCKS = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, CrittersAndCompanions.MODID);

    public static final Supplier<Block> SILK_COCOON = BLOCKS.register("silk_cocoon", () -> new SilkCocoonBlock(BlockBehaviour.Properties.of().pushReaction(PushReaction.DESTROY).instabreak().sound(SoundType.WOOL)));
    public static final Supplier<Block> SEA_BUNNY_SLIME_BLOCK = BLOCKS.register("sea_bunny_slime_block", () -> new SeaBunnySlimeBlock(BlockBehaviour.Properties.of().sound(SoundType.SLIME_BLOCK).noOcclusion().dynamicShape()));
}