package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACBlocks;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.stream.Stream;

public final class CACRecipeProvider extends RecipeProvider {

    private static final TagKey<Item> C_IRON_INGOTS = cTag("iron_ingots");
    private static final TagKey<Item> C_GOLD_INGOTS = cTag("gold_ingots");
    private static final TagKey<Item> C_DIAMONDS = cTag( "diamonds");
    private static final TagKey<Item> C_SLIMEBALLS = cTag( "slimeballs");

    public CACRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    private static TagKey<Item> cTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
    }

    private static Ingredient commonIngredient(TagKey<Item> forgeTag, TagKey<Item> fabricTag, Item fallback) {
        return Ingredient.fromValues(Stream.of(
                new Ingredient.TagValue(forgeTag),
                new Ingredient.TagValue(fabricTag),
                new Ingredient.ItemValue(new ItemStack(fallback))
        ));
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ENDER_PEARL)
            .requires(CACItems.PEARL.get())
            .requires(Items.POPPED_CHORUS_FRUIT)
            .unlockedBy("has_pearl", has(CACItems.PEARL.get()))
            .save(consumer, CrittersAndCompanions.createId("ender_pearl_from_pearl"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CACItems.IRON_DRAGONFLY_ARMOR.get())
                .pattern("  I")
                .pattern(" I ")
                .pattern("I  ")
                .define('I', commonIngredient(Tags.Items.INGOTS_IRON, C_IRON_INGOTS, Items.IRON_INGOT))
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CACItems.GRAPPLING_HOOK.get())
                .pattern("SS ")
                .pattern("SI ")
                .pattern("  S")
                .define('S', CACItems.SILK.get())
                .define('I', commonIngredient(Tags.Items.INGOTS_IRON, C_IRON_INGOTS, Items.IRON_INGOT))
                .unlockedBy("has_silk", has(CACItems.SILK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CACItems.PEARL_NECKLACE_1.get())
                .pattern(" P ")
                .pattern("P P")
                .pattern(" P ")
                .define('P', CACItems.PEARL.get())
                .unlockedBy("has_pearl", has(CACItems.PEARL.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STRING)
                .requires(CACItems.SILK.get())
                .unlockedBy("has_silk", has(CACItems.SILK.get()))
                .save(consumer, CrittersAndCompanions.createId("string_from_silk"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CACItems.PEARL_NECKLACE_3.get())
                .requires(CACItems.PEARL_NECKLACE_2.get())
                .requires(CACItems.PEARL.get())
                .unlockedBy("has_pearl_necklace_2", has(CACItems.PEARL_NECKLACE_2.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CACItems.GOLD_DRAGONFLY_ARMOR.get())
                .pattern("  G")
                .pattern(" G ")
                .pattern("G  ")
                .define('G', commonIngredient(Tags.Items.INGOTS_GOLD, C_GOLD_INGOTS, Items.GOLD_INGOT))
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CACItems.ACORN_HAT.get())
                .pattern("AAA")
                .pattern("A A")
                .define('A', CACItems.ACORN.get())
                .unlockedBy("has_acorn", has(CACItems.ACORN.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CACItems.PEARL_NECKLACE_2.get())
                .requires(CACItems.PEARL_NECKLACE_1.get())
                .requires(CACItems.PEARL.get())
                .unlockedBy("has_pearl_necklace_1", has(CACItems.PEARL_NECKLACE_1.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SLIME_BALL)
                .requires(CACItems.SEA_BUNNY_SLIME_BOTTLE.get())
                .unlockedBy("has_sea_bunny_slime_bottle", has(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()))
                .save(consumer, CrittersAndCompanions.createId("slime_ball_from_sea_bunny_slime_bottle"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CACBlocks.SEA_BUNNY_SLIME_BLOCK.get())
                .pattern("SS")
                .pattern("SS")
                .define('S', CACItems.SEA_BUNNY_SLIME_BOTTLE.get())
                .unlockedBy("has_sea_bunny_slime_bottle", has(CACItems.SEA_BUNNY_SLIME_BOTTLE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CACItems.DIAMOND_DRAGONFLY_ARMOR.get())
                .pattern("  D")
                .pattern(" D ")
                .pattern("D  ")
                .define('D', commonIngredient(Tags.Items.GEMS_DIAMOND, C_DIAMONDS, Items.DIAMOND))
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CACItems.SILK_LEAD.get())
                .pattern("SS ")
                .pattern("SB ")
                .pattern("  S")
                .define('S', CACItems.SILK.get())
                .define('B', commonIngredient(Tags.Items.SLIMEBALLS, C_SLIMEBALLS, Items.SLIME_BALL))
                .unlockedBy("has_silk", has(CACItems.SILK.get()))
                .save(consumer);
    }
}
