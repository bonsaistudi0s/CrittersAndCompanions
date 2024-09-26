package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Drowned.class)
public class DrownedMixin {

    @Inject(
            method = "finalizeSpawn",
            at = @At("RETURN")
    )
    private void addClam(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData, CompoundTag compoundTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        var self = (Drowned) (Object) this;

        if (self.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() && self.getRandom().nextFloat() < 0.05F) {
            self.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(CACItems.CLAM.get()));
            self.setGuaranteedDrop(EquipmentSlot.OFFHAND);
        }
    }

}
