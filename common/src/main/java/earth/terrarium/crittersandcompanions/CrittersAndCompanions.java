package earth.terrarium.crittersandcompanions;


import earth.terrarium.crittersandcompanions.common.registry.ModBlocks;
import earth.terrarium.crittersandcompanions.common.registry.ModEntities;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import earth.terrarium.crittersandcompanions.common.registry.ModSounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrittersAndCompanions {

    public static final String MODID = "crittersandcompanions";
    private static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        System.out.println("CrittersAndCompanions Init begun");
        ModBlocks.BLOCKS.init();
        ModEntities.ENTITIES.init();
        System.out.println("CrittersAndCompanions Entities initialized");
        ModItems.ITEMS.init();
        System.out.println("CrittersAndCompanions Items initialized");
        ModSounds.SOUNDS.init();
        ModItems.TABS.init();
    }
}
