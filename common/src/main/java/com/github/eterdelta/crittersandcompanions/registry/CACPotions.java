package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import com.github.eterdelta.crittersandcompanions.platform.RegistryHelper;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

public class CACPotions {

    private static final RegistryHelper<Potion> POTIONS =
            Services.PLATFORM.createRegistryHelper(Registries.POTION, CrittersAndCompanions.MODID);

    public static final RegistryEntry<Potion> RESISTANCE = POTIONS.register(
            "resistance",
            () -> new Potion(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 3, 0)));

    public static final RegistryEntry<Potion> LONG_RESISTANCE = POTIONS.register(
            "long_resistance",
            () -> new Potion("resistance", new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 8, 0)));

    public static final RegistryEntry<Potion> STRONG_RESISTANCE = POTIONS.register(
            "strong_resistance",
            () -> new Potion("resistance", new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 + 20 * 30, 1)));

    public static void init() {
        // load class
    }

    /**
     * Matches {@link PotionBrewing.Builder#addMix(Holder, Item, Holder)}
     */
    @FunctionalInterface
    public interface BrewingAdder {
        void addMix(Holder<Potion> input, Item ingredient, Holder<Potion> output);
    }

    public static void registerBrewingRecipes(BrewingAdder adder) {
        var potions = BuiltInRegistries.POTION;
        adder.addMix(
                Potions.AWKWARD,
                CACItems.SNAIL_SLIME_BOTTLE.get(),
                potions.getHolderOrThrow(CACPotions.RESISTANCE.getKey())
        );
        adder.addMix(
                potions.getHolderOrThrow(CACPotions.RESISTANCE.getKey()),
                Items.GLOWSTONE_DUST,
                potions.getHolderOrThrow(CACPotions.STRONG_RESISTANCE.getKey())
        );
        adder.addMix(
                potions.getHolderOrThrow(CACPotions.RESISTANCE.getKey()),
                Items.REDSTONE,
                potions.getHolderOrThrow(CACPotions.LONG_RESISTANCE.getKey())
        );
    }
}
