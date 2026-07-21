package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACBlocks;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public final class CACItemModels extends ItemModelProvider {

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
        spawnEggItem(CACItems.ROLY_POLY_SPAWN_EGG.get());
        spawnEggItem(CACItems.SNAIL_SPAWN_EGG.get());
        spawnEggItem(CACItems.STICK_BUG_SPAWN_EGG.get());
        spawnEggItem(CACItems.WEEVIL_SPAWN_EGG.get());

        simpleBlockItem(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get());
        simpleBlockItem(CACBlocks.SILK_COCOON.get());

        basicItem(CACItems.SILK.get());
        basicItem(CACItems.SILK_LEAD.get());
        basicItem(CACItems.KOI_FISH.get());
        basicItem(CACItems.KOI_FISH_BUCKET.get());
        basicItem(CACItems.PEARL.get());
        basicItem(CACItems.DRAGONFLY_WING.get());
        basicItem(CACItems.IRON_DRAGONFLY_ARMOR.get());
        basicItem(CACItems.GOLD_DRAGONFLY_ARMOR.get());
        basicItem(CACItems.DIAMOND_DRAGONFLY_ARMOR.get());
        basicItem(CACItems.GRAPPLING_HOOK.get());
        basicItem(CACItems.CLAM.get());
        basicItem(CACItems.SEA_BUNNY_SLIME_BOTTLE.get());
        basicItem(CACItems.ACORN.get());
        basicItem(CACItems.ACORN_HAT.get());
        basicItem(CACItems.SNAIL_SLIME_BOTTLE.get());
    }

    private ItemModelBuilder spawnEggItem(Item item) {
        var key = ForgeRegistries.ITEMS.getKey(item);
        return withExistingParent(key.getPath(), mcLoc("item/template_spawn_egg"));
    }

    private ItemModelBuilder simpleBlockItem(Block block) {
        var key = ForgeRegistries.BLOCKS.getKey(block);
        return withExistingParent(key.getPath(), modLoc("block/" + key.getPath()));
    }
}
