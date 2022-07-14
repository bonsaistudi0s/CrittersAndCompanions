package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.registry.CaCItems;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.ArrayUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Ocelot.class)
public class OcelotMixin {

    @Final
    @Shadow
    private static Ingredient TEMPT_INGREDIENT;

    @Redirect(at = @At(value = "FIELD", target = "net/minecraft/world/entity/animal/Ocelot.TEMPT_INGREDIENT:Lnet/minecraft/world/item/crafting/Ingredient;", opcode = Opcodes.GETSTATIC), method = {"registerGoals()V", "isFood(Lnet/minecraft/world/item/ItemStack;)Z"})
    private Ingredient setTemptIngredient() {
        return Ingredient.of(ArrayUtils.add(TEMPT_INGREDIENT.getItems(), CaCItems.KOI_FISH.get().getDefaultInstance()));
    }
}
