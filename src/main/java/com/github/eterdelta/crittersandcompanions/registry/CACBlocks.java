package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.blocks.SilkCocoonBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CACBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CrittersAndCompanions.MODID);

    public static final RegistryObject<Block> SILK_COCOON = BLOCKS.register("silk_cocoon", () -> new SilkCocoonBlock(BlockBehaviour.Properties.of(Material.WEB).instabreak().sound(SoundType.WOOL)));
}