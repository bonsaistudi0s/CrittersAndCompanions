package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.block.SeaBunnySlimeBlock;
import com.github.eterdelta.crittersandcompanions.block.SilkCocoonBlock;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import com.github.eterdelta.crittersandcompanions.platform.RegistryHelper;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public class CACBlocks {
    private static final RegistryHelper<Block> BLOCKS = Services.PLATFORM.createRegistryHelper(Registries.BLOCK, CrittersAndCompanions.MODID);

    public static final RegistryEntry<Block> SILK_COCOON = BLOCKS.register("silk_cocoon", () -> new SilkCocoonBlock(BlockBehaviour.Properties.of().instabreak().sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));
    public static final RegistryEntry<Block> SEA_BUNNY_SLIME_BLOCK = BLOCKS.register("sea_bunny_slime_block", () -> new SeaBunnySlimeBlock(BlockBehaviour.Properties.of().noOcclusion().dynamicShape().sound(SoundType.SLIME_BLOCK)));

    public static void init() {
        // Load the class
    }
}