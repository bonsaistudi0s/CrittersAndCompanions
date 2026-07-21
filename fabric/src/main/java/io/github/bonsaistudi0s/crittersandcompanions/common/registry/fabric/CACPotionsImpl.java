package io.github.bonsaistudi0s.crittersandcompanions.common.registry.fabric;

import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACPotions;
import net.minecraft.world.item.alchemy.PotionBrewing;

import java.util.List;

@SuppressWarnings("unused")
public class CACPotionsImpl {

    public static void registerBrewingRecipes(List<CACPotions.BrewingMix> mixes) {
        for (var mix : mixes) {
            PotionBrewing.addMix(
                    mix.inputPotion(),
                    mix.ingredient(),
                    mix.outputPotion()
            );
        }
    }
}
