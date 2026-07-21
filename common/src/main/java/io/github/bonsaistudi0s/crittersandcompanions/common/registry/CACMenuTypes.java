package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.menu.RolyPolyMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class CACMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(CrittersAndCompanions.MODID, Registries.MENU);

    public static final RegistrySupplier<MenuType<RolyPolyMenu>> ROLY_POLY_CHEST = MENUS.register(
            "roly_poly_chest",
            () -> new MenuType<>(RolyPolyMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static void init() {
        MENUS.register();
    }
}
