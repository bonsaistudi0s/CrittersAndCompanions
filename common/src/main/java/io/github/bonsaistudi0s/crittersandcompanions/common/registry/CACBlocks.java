package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.block.SeaBunnySlimeBlock;
import io.github.bonsaistudi0s.crittersandcompanions.common.block.SilkCocoonBlock;

public class CACBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(CrittersAndCompanions.MODID, Registries.BLOCK);

    public static final RegistrySupplier<Block> SILK_COCOON = BLOCKS.register("silk_cocoon", () -> new SilkCocoonBlock(BlockBehaviour.Properties.of().instabreak().sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));
    public static final RegistrySupplier<Block> SEA_BUNNY_SLIME_BLOCK = BLOCKS.register("sea_bunny_slime_block", () -> new SeaBunnySlimeBlock(BlockBehaviour.Properties.of().noOcclusion().dynamicShape().sound(SoundType.SLIME_BLOCK)));

    public static void init() {
        BLOCKS.register();
    }
}