package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACBlocks;

public final class CACRecipes extends RecipeProvider {

    public CACRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ENDER_PEARL)
                .requires(CACItems.PEARL.get())
                .requires(Items.POPPED_CHORUS_FRUIT)
                .unlockedBy("has_pearl", has(CACItems.PEARL.get()))
                .save(output, CrittersAndCompanions.createId("ender_pearl_from_pearl"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CACItems.IRON_DRAGONFLY_ARMOR.get())
                .pattern("  I")
                .pattern(" I ")
                .pattern("I  ")
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CACItems.GRAPPLING_HOOK.get())
                .pattern("SS ")
                .pattern("SI ")
                .pattern("  S")
                .define('S', CACItems.SILK.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_silk", has(CACItems.SILK.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CACItems.PEARL_NECKLACE_1.get())
                .pattern(" P ")
                .pattern("P P")
                .pattern(" P ")
                .define('P', CACItems.PEARL.get())
                .unlockedBy("has_pearl", has(CACItems.PEARL.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING)
                .requires(CACItems.SILK.get())
                .unlockedBy("has_silk", has(CACItems.SILK.get()))
                .save(output, CrittersAndCompanions.createId("string_from_silk"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CACItems.PEARL_NECKLACE_3.get())
                .requires(CACItems.PEARL_NECKLACE_2.get())
                .requires(CACItems.PEARL.get())
                .unlockedBy("has_pearl_necklace_2", has(CACItems.PEARL_NECKLACE_2.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CACItems.GOLD_DRAGONFLY_ARMOR.get())
                .pattern("  G")
                .pattern(" G ")
                .pattern("G  ")
                .define('G', Tags.Items.INGOTS_GOLD)
                .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CACItems.ACORN_HAT.get())
                .pattern("AAA")
                .pattern("A A")
                .define('A', CACItems.ACORN.get())
                .unlockedBy("has_acorn", has(CACItems.ACORN.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CACItems.PEARL_NECKLACE_2.get())
                .requires(CACItems.PEARL_NECKLACE_1.get())
                .requires(CACItems.PEARL.get())
                .unlockedBy("has_pearl_necklace_1", has(CACItems.PEARL_NECKLACE_1.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SLIME_BALL)
                .requires(CACItems.SEA_BUNNY_SLIME_BOTTLE.get())
                .unlockedBy("has_sea_bunny_slime_bottle", has(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()))
                .save(output, CrittersAndCompanions.createId("slime_ball_from_sea_bunny_slime_bottle"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CACBlocks.SEA_BUNNY_SLIME_BLOCK.get())
                .pattern("SS")
                .pattern("SS")
                .define('S', CACItems.SEA_BUNNY_SLIME_BOTTLE.get())
                .unlockedBy("has_sea_bunny_slime_bottle", has(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CACItems.DIAMOND_DRAGONFLY_ARMOR.get())
                .pattern("  D")
                .pattern(" D ")
                .pattern("D  ")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CACItems.SILK_LEAD.get())
                .pattern("SS ")
                .pattern("SB ")
                .pattern("  S")
                .define('S', CACItems.SILK.get())
                .define('B', Tags.Items.SLIME_BALLS)
                .unlockedBy("has_silk", has(CACItems.SILK.get()))
                .save(output);
    }
}
