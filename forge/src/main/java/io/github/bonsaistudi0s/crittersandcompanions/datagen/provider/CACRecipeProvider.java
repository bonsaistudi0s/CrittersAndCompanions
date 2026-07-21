package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider;

import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class CACRecipeProvider extends RecipeProvider {

    public CACRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CACItems.ACORN_HAT.get())
                .pattern("AAA")
                .pattern("A A")
                .define('A', CACItems.ACORN.get())
                .unlockedBy("has_acorn", has(CACItems.ACORN.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CACItems.DIAMOND_DRAGONFLY_ARMOR.get())
                .pattern("  D")
                .pattern(" D ")
                .pattern("D  ")
                .define('D', Items.DIAMOND)
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CACItems.GOLD_DRAGONFLY_ARMOR.get())
                .pattern("  G")
                .pattern(" G ")
                .pattern("G  ")
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CACItems.IRON_DRAGONFLY_ARMOR.get())
                .pattern("  I")
                .pattern(" I ")
                .pattern("I  ")
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CACItems.GRAPPLING_HOOK.get())
                .pattern("SS ")
                .pattern("SI ")
                .pattern("  S")
                .define('S', CACItems.SILK.get())
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_silk", has(CACItems.SILK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CACItems.SILK_LEAD.get())
                .pattern("SS ")
                .pattern("SB ")
                .pattern("  S")
                .define('S', CACItems.SILK.get())
                .define('B', Items.SLIME_BALL)
                .unlockedBy("has_silk", has(CACItems.SILK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CACItems.PEARL_NECKLACE_1.get())
                .pattern(" P ")
                .pattern("P P")
                .pattern(" P ")
                .define('P', CACItems.PEARL.get())
                .unlockedBy("has_pearl", has(CACItems.PEARL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CACItems.SEA_BUNNY_SLIME_BLOCK.get())
                .pattern("SS")
                .pattern("SS")
                .define('S', CACItems.SEA_BUNNY_SLIME_BOTTLE.get())
                .unlockedBy("has_sea_bunny_slime", has(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ENDER_PEARL)
                .requires(CACItems.PEARL.get())
                .requires(Items.POPPED_CHORUS_FRUIT)
                .unlockedBy("has_pearl", has(CACItems.PEARL.get()))
                .save(consumer, "crittersandcompanions:ender_pearl_from_pearl");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SLIME_BALL)
                .requires(CACItems.SEA_BUNNY_SLIME_BOTTLE.get())
                .unlockedBy("has_sea_bunny_slime", has(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()))
                .save(consumer, "crittersandcompanions:slime_ball_from_sea_bunny_slime");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING)
                .requires(CACItems.SILK.get())
                .unlockedBy("has_silk", has(CACItems.SILK.get()))
                .save(consumer, "crittersandcompanions:string_from_silk");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, CACItems.PEARL_NECKLACE_2.get())
                .requires(CACItems.PEARL_NECKLACE_1.get())
                .requires(CACItems.PEARL.get())
                .unlockedBy("has_pearl_necklace_1", has(CACItems.PEARL_NECKLACE_1.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, CACItems.PEARL_NECKLACE_3.get())
                .requires(CACItems.PEARL_NECKLACE_2.get())
                .requires(CACItems.PEARL.get())
                .unlockedBy("has_pearl_necklace_2", has(CACItems.PEARL_NECKLACE_2.get()))
                .save(consumer);
    }
}
