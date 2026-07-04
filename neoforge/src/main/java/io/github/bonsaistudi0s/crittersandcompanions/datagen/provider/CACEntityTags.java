package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;

public final class CACEntityTags extends EntityTypeTagsProvider {

    public CACEntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, CrittersAndCompanions.MODID, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var underWater = new EntityType[]{
                CACEntities.DUMBO_OCTOPUS.get(),
                CACEntities.KOI_FISH.get(),
                CACEntities.SEA_BUNNY.get()
        };

        tag(EntityTypeTags.AQUATIC).add(underWater);
        tag(EntityTypeTags.AXOLOTL_HUNT_TARGETS).add(underWater);

        tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(CACEntities.OTTER.get())
                .add(underWater);

        tag(EntityTypeTags.ARTHROPOD)
                .add(CACEntities.JUMPING_SPIDER.get())
                .add(CACEntities.LEAF_INSECT.get())
                .add(CACEntities.LADYBUG.get())
                .add(CACEntities.STAG_BEETLE.get())
                .add(CACEntities.ROLY_POLY.get())
                .add(CACEntities.STICK_BUG.get())
                .add(CACEntities.WEEVIL.get())
                .add(CACEntities.DRAGONFLY.get());
    }

}
