package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;

public class CACArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(CrittersAndCompanions.MODID, Registries.ARMOR_MATERIAL);

    public static final RegistrySupplier<ArmorMaterial> ACORN = ARMOR_MATERIALS.register(
            "acorn",
            () -> new ArmorMaterial(
                    Map.of(
                            ArmorItem.Type.HELMET, 1,
                            ArmorItem.Type.CHESTPLATE, 0,
                            ArmorItem.Type.LEGGINGS, 0,
                            ArmorItem.Type.BOOTS, 0
                    ),
                    15,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(CACItems.ACORN.get()),
                    List.of(new ArmorMaterial.Layer(CrittersAndCompanions.createId("acorn"))),
                    0.0F,
                    0.0F
            )
    );

    public static void init() {
        ARMOR_MATERIALS.register();
    }
}
