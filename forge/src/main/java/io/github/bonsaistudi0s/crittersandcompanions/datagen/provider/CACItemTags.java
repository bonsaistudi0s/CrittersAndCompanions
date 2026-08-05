package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.AnimalTags;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class CACItemTags extends ItemTagsProvider {

    public CACItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, blockTags, CrittersAndCompanions.MODID, fileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(ItemTags.FISHES).add(CACItems.KOI_FISH.get());

        tag(DragonflyEntity.TAGS.food()).add(Items.SPIDER_EYE);
        temptWithFood(DragonflyEntity.TAGS);

        tag(JumpingSpiderEntity.TAGS.food()).add(CACItems.DRAGONFLY_WING.get());
        temptWithFood(JumpingSpiderEntity.TAGS);

        tag(FerretEntity.TAGS.food()).add(Items.CHICKEN);
        tag(FerretEntity.TAGS.tempt()).add(Items.RABBIT);

        tag(OtterEntity.TAGS.food())
                .addTag(ItemTags.FISHES)
                .add(CACItems.CLAM.get());

        tag(RedPandaEntity.TAGS.food()).add(Items.BAMBOO);
        tag(RedPandaEntity.TAGS.tempt()).add(Items.SWEET_BERRIES);

        tag(ShimaEnagaEntity.TAGS.food()).add(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS).addOptionalTag(Tags.Items.SEEDS).addOptionalTag(cTag("seeds"));
        temptWithFood(ShimaEnagaEntity.TAGS);

        tag(RolyPolyEntity.TAGS.tempt()).add(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM).addOptionalTag(Tags.Items.MUSHROOMS).addOptionalTag(cTag("mushrooms"));
        tag(RolyPolyEntity.TAGS.food()).add(Items.DEAD_BUSH);

        tag(LadybugEntity.TAGS.food()).add(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM).addOptionalTag(Tags.Items.MUSHROOMS).addOptionalTag(cTag("mushrooms"));
        temptWithFood(LadybugEntity.TAGS);

        tag(StagBeetleEntity.TAGS.food()).add(Items.SWEET_BERRIES, Items.GLOW_BERRIES);
        temptWithFood(StagBeetleEntity.TAGS);

        tag(StickBugEntity.TAGS.food()).addTag(ItemTags.LEAVES);
        temptWithFood(StickBugEntity.TAGS);

        tag(SnailEntity.TAGS.tempt()).addTag(ItemTags.LEAVES);
        tag(SnailEntity.TAGS.food()).add(Items.CARROT);

        tag(WeevilEntity.TAGS.food()).add(CACItems.ACORN.get());
        temptWithFood(WeevilEntity.TAGS);

        tag(LeafInsectEntity.TAGS.food()).addTag(ItemTags.LEAVES);
        temptWithFood(LeafInsectEntity.TAGS);

        tag(SeaBunnyEntity.TAGS.food()).add(Items.SPONGE, Items.WET_SPONGE);
        temptWithFood(SeaBunnyEntity.TAGS);

        tag(CACTags.WOODEN_CHESTS).add(Items.CHEST, Items.TRAPPED_CHEST).addOptionalTag(Tags.Items.CHESTS_WOODEN).addOptionalTag(cTag("wooden_chests"));

        tag(TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace("enchantable/durability"))).add(CACItems.GRAPPLING_HOOK.get());
    }

    private void temptWithFood(AnimalTags tags) {
        tag(tags.tempt()).addTag(tags.food());
    }

    private TagKey<Item> cTag(String tag) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", tag));
    }
}
