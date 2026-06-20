package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.menu.RolyPolyMenu;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import com.github.eterdelta.crittersandcompanions.platform.RegistryHelper;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.MenuType;

public class CACMenuTypes {

    private static final RegistryHelper<MenuType<?>> REGISTRY =
            Services.PLATFORM.createRegistryHelper(Registries.MENU, CrittersAndCompanions.MODID);

    public static final RegistryEntry<MenuType<RolyPolyMenu>> ROLY_POLY_CHEST = REGISTRY.register(
            "roly_poly_chest",
            () -> Services.PLATFORM.createMenuType(
                    (id, inv) -> new RolyPolyMenu(id, inv, new SimpleContainer(RolyPolyMenu.SLOTS), null)
            )
    );

    public static void init() {
        // load class
    }
}
