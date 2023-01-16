package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.item.PearlNecklaceItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(at = @At(value = "LOAD", ordinal = 3), method = "travel(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 1)
    private float modifySwimSpeed(float swimSpeed) {
        if (((Entity) this) instanceof Player player) {
            Inventory inventory = player.getInventory();

            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack stack = inventory.getItem(i);
                if (stack.getItem() instanceof PearlNecklaceItem pearlNecklaceItem) {
                    return swimSpeed + (swimSpeed * ((pearlNecklaceItem.getLevel() * 20) / 100.0F));
                }
            }
        }
        return swimSpeed;
    }
}
