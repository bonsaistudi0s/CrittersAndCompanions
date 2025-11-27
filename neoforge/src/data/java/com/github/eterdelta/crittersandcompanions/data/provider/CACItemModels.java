package com.github.eterdelta.crittersandcompanions.data.provider;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CACItemModels extends ItemModelProvider {

    public CACItemModels(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, CrittersAndCompanions.MODID, fileHelper);
    }

    @Override
    protected void registerModels() {
        spawnEggItem(CACItems.LADYBUG_SPAWN_EGG.get());
        spawnEggItem(CACItems.DRAGONFLY_SPAWN_EGG.get());
        spawnEggItem(CACItems.FERRET_SPAWN_EGG.get());
        spawnEggItem(CACItems.OTTER_SPAWN_EGG.get());
        spawnEggItem(CACItems.KOI_FISH_SPAWN_EGG.get());
        spawnEggItem(CACItems.LEAF_INSECT_SPAWN_EGG.get());
        spawnEggItem(CACItems.JUMPING_SPIDER_SPAWN_EGG.get());
        spawnEggItem(CACItems.SEA_BUNNY_SPAWN_EGG.get());
        spawnEggItem(CACItems.SHIMA_ENAGA_SPAWN_EGG.get());
        spawnEggItem(CACItems.RED_PANDA_SPAWN_EGG.get());
        spawnEggItem(CACItems.STAG_BEETLE_SPAWN_EGG.get());
        spawnEggItem(CACItems.DUMBO_OCTOPUS_SPAWN_EGG.get());
        spawnEggItem(CACItems.ROLLYPOLLY_SPAWN_EGG.get());
    }

}
