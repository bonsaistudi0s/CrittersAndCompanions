package earth.terrarium.crittersandcompanions.fabric;


import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class CrittersAndCompanionsFabric {

    public static void init() {
        CrittersAndCompanions.init();

        ItemGroupEvents.modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(CrittersAndCompanions.MODID, "main"))).register(group -> {
            ModItems.ITEMS.stream().map(Supplier::get).forEach(group::accept);
        });
    }
}
