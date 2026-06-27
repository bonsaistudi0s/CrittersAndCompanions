package com.github.eterdelta.crittersandcompanions.data.provider;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.FerretEntity;
import com.github.eterdelta.crittersandcompanions.registry.CACTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class CACBlockTags extends BlockTagsProvider {

    public CACBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, CrittersAndCompanions.MODID, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(FerretEntity.DIG_GROUNDS_TAG)
                .addTag(BlockTags.DIRT)
                .addTag(BlockTags.SAND)
                .add(Blocks.GRAVEL);

        tag(CACTags.BUG_SPAWN_GROUND)
                .add(Blocks.MOSS_BLOCK)
                .add(Blocks.GRASS_BLOCK)
                .add(Blocks.PODZOL)
                .add(Blocks.ROOTED_DIRT);
    }
}
