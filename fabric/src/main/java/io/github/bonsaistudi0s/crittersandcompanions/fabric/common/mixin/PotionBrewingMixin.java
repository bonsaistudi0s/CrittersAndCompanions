package io.github.bonsaistudi0s.crittersandcompanions.fabric.common.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.alchemy.PotionBrewing;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACPotions;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {

    @Inject(
            method = "bootstrap",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/alchemy/PotionBrewing$Builder;build()" +
                            "Lnet/minecraft/world/item/alchemy/PotionBrewing;"
            )
    )
    private static void addCACRecipes(FeatureFlagSet featureFlagSet, CallbackInfoReturnable<PotionBrewing> cir,
                                      @Local PotionBrewing.Builder builder) {
        CACPotions.registerBrewingRecipes(builder::addMix);
    }
}
