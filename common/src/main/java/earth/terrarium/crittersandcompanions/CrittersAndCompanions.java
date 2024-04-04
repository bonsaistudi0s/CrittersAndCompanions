package earth.terrarium.crittersandcompanions;


import earth.terrarium.crittersandcompanions.common.network.NetworkHandler;
import earth.terrarium.crittersandcompanions.common.registry.ModBlocks;
import earth.terrarium.crittersandcompanions.common.registry.ModEntities;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import earth.terrarium.crittersandcompanions.common.registry.ModSounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrittersAndCompanions {

    public static final String MODID = "crittersandcompanions";
    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        ModBlocks.BLOCKS.init();
        ModEntities.ENTITIES.init();
        ModItems.ITEMS.init();
        ModSounds.SOUNDS.init();
        ModItems.TABS.init();

        NetworkHandler.registerPackets();
    }
}
