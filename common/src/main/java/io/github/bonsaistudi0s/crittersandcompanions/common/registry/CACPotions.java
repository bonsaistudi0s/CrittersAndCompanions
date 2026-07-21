package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.List;

public class CACPotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(CrittersAndCompanions.MODID, Registries.POTION);
    public static final List<BrewingMix> BREWING_MIXES = new ArrayList<>();

    public static final RegistrySupplier<Potion> RESISTANCE = POTIONS.register(
            "resistance",
            () -> new Potion(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 3, 0)));

    public static final RegistrySupplier<Potion> LONG_RESISTANCE = POTIONS.register(
            "long_resistance",
            () -> new Potion("resistance", new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 * 8, 0)));

    public static final RegistrySupplier<Potion> STRONG_RESISTANCE = POTIONS.register(
            "strong_resistance",
            () -> new Potion("resistance", new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 60 + 20 * 30, 1)));

    public static void init() {
        POTIONS.register();
    }

    public static void initializeMixes() {
        BREWING_MIXES.add(new BrewingMix(
                CACItems.SNAIL_SLIME_BOTTLE.get(),
                Potions.AWKWARD,
                RESISTANCE.get()
        ));

        BREWING_MIXES.add(new BrewingMix(
                Items.GLOWSTONE_DUST,
                RESISTANCE.get(),
                STRONG_RESISTANCE.get()
        ));

        BREWING_MIXES.add(new BrewingMix(
                Items.REDSTONE,
                RESISTANCE.get(),
                LONG_RESISTANCE.get()
        ));
    }

    @ExpectPlatform
    public static void registerBrewingRecipes(List<BrewingMix> mixes) {
        throw new AssertionError();
    }

    public record BrewingMix(Item ingredient, Potion inputPotion, Potion outputPotion) {
    }
}
