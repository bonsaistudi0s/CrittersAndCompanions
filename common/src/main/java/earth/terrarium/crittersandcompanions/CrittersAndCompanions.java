package earth.terrarium.crittersandcompanions;


import earth.terrarium.crittersandcompanions.common.registry.ModBlocks;
import earth.terrarium.crittersandcompanions.common.registry.ModEntities;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import earth.terrarium.crittersandcompanions.common.registry.ModSounds;

public class CrittersAndCompanions {

    public static final String MODID = "crittersandcompanions";

    public static void init() {
        ModBlocks.BLOCKS.init();
        ModEntities.ENTITIES.init();
        ModItems.ITEMS.init();
        ModSounds.SOUNDS.init();
        ModItems.TABS.init();
    }
}
