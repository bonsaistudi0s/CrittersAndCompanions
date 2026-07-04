package io.github.bonsaistudi0s.crittersandcompanions.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;

@Mixin(Mob.class)
public abstract class MobMixin {

    @ModifyExpressionValue(
            method = "checkAndHandleImportantInteractions(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    public boolean checkAndHandleImportantInteractions(boolean original, @Local ItemStack stack) {
        return original || stack.is(CACItems.SILK_LEAD.get());
    }

}