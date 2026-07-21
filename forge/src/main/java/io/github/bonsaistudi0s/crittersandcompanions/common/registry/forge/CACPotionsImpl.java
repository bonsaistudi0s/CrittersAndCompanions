package io.github.bonsaistudi0s.crittersandcompanions.common.registry.forge;

import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACPotions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import java.util.List;

@SuppressWarnings("unused")
public class CACPotionsImpl {

    public static void registerBrewingRecipes(List<CACPotions.BrewingMix> mixes) {
        for (var mix : mixes) {
            var inputStack = PotionUtils.setPotion(new ItemStack(Items.POTION), mix.inputPotion());
            var outputStack = PotionUtils.setPotion(new ItemStack(Items.POTION), mix.outputPotion());

            BrewingRecipeRegistry.addRecipe(
                    Ingredient.of(inputStack),
                    Ingredient.of(mix.ingredient()),
                    outputStack
            );
        }
    }
}
