package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Cat.class)
public class CatMixin {

    @Final
    @Shadow
    private static Ingredient TEMPT_INGREDIENT;

    @Redirect(at = @At(value = "FIELD", target = "net/minecraft/world/entity/animal/Cat.TEMPT_INGREDIENT:Lnet/minecraft/world/item/crafting/Ingredient;", opcode = 178 /* Opcodes.GETSTATIC */), method = {"registerGoals()V", "isFood(Lnet/minecraft/world/item/ItemStack;)Z"})
    private Ingredient setTemptIngredient() {
        return Ingredient.of(ArrayUtils.add(TEMPT_INGREDIENT.getItems(), CACItems.KOI_FISH.get().getDefaultInstance()));
    }
}
