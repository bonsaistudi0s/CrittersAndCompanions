package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import com.github.eterdelta.crittersandcompanions.platform.RegistryHelper;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class CACArmorMaterials {

    private static final RegistryHelper<ArmorMaterial> ARMOR_MATERIALS =
            Services.PLATFORM.createRegistryHelper(Registries.ARMOR_MATERIAL, CrittersAndCompanions.MODID);

    public static final RegistryEntry<ArmorMaterial> ACORN = ARMOR_MATERIALS.register(
            "acorn",
            () -> new ArmorMaterial(
                    Map.of(
                            ArmorItem.Type.HELMET, 1,
                            ArmorItem.Type.CHESTPLATE, 0,
                            ArmorItem.Type.LEGGINGS, 0,
                            ArmorItem.Type.BOOTS, 0),
                    15,
                    SoundEvents.ARMOR_EQUIP_LEATHER,
                    () -> Ingredient.of(CACItems.ACORN.get()),
                    List.of(new ArmorMaterial.Layer(CrittersAndCompanions.createId("acorn"))),
                    0.0F,
                    0.0F));

    public static void init() {
        // static init
    }
}
