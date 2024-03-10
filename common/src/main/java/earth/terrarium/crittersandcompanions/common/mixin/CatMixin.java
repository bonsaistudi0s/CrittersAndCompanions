package earth.terrarium.crittersandcompanions.common.mixin;

import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.ArrayUtils;
import org.objectweb.asm.Opcodes;
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

    @Redirect(at = @At(value = "FIELD", target = "net/minecraft/world/entity/animal/Cat.TEMPT_INGREDIENT:Lnet/minecraft/world/item/crafting/Ingredient;", opcode = Opcodes.GETSTATIC), method = {"registerGoals()V", "isFood(Lnet/minecraft/world/item/ItemStack;)Z"})
    private Ingredient setTemptIngredient() {
        return Ingredient.of(ArrayUtils.add(TEMPT_INGREDIENT.getItems(), ModItems.KOI_FISH.get().getDefaultInstance()));
    }
}
