package com.github.eterdelta.crittersandcompanions.data.provider;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import com.github.eterdelta.crittersandcompanions.entity.FerretEntity;
import com.github.eterdelta.crittersandcompanions.entity.JumpingSpiderEntity;
import com.github.eterdelta.crittersandcompanions.entity.LadybugEntity;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import com.github.eterdelta.crittersandcompanions.entity.RedPandaEntity;
import com.github.eterdelta.crittersandcompanions.entity.RollypollyEntity;
import com.github.eterdelta.crittersandcompanions.entity.ShimaEnagaEntity;
import com.github.eterdelta.crittersandcompanions.entity.SnailEntity;
import com.github.eterdelta.crittersandcompanions.entity.StagBeetleEntity;
import com.github.eterdelta.crittersandcompanions.entity.StickBugEntity;
import com.github.eterdelta.crittersandcompanions.entity.WeevilEntity;
import com.github.eterdelta.crittersandcompanions.registry.AnimalTags;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class CACItemTags extends ItemTagsProvider {

    public CACItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, blockTags, CrittersAndCompanions.MODID, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.CAT_FOOD).add(CACItems.KOI_FISH.get());
        tag(ItemTags.OCELOT_FOOD).add(CACItems.KOI_FISH.get());
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

        tag(ShimaEnagaEntity.TAGS.food()).addTag(Tags.Items.SEEDS);
        temptWithFood(ShimaEnagaEntity.TAGS);

        tag(RollypollyEntity.TAGS.tempt()).addTag(Tags.Items.MUSHROOMS);
        tag(RollypollyEntity.TAGS.food()).add(Items.DEAD_BUSH);

        tag(LadybugEntity.TAGS.food()).addTag(Tags.Items.MUSHROOMS);
        temptWithFood(LadybugEntity.TAGS);

        tag(StagBeetleEntity.TAGS.food()).addTag(Tags.Items.FOODS_BERRY);
        temptWithFood(StagBeetleEntity.TAGS);

        tag(StickBugEntity.TAGS.food()).addTag(ItemTags.LEAVES);
        temptWithFood(StickBugEntity.TAGS);

        tag(SnailEntity.TAGS.tempt()).addTag(ItemTags.LEAVES);
        tag(SnailEntity.TAGS.food()).add(Items.CARROT);

        //tag(WeevilEntity.TAGS.food()).addTag(CACItems.ACORN);
        //temptWithFood(WeevilEntity.TAGS);
    }

    public void temptWithFood(AnimalTags tags) {
        tag(tags.tempt()).addTag(tags.food());
    }

}
