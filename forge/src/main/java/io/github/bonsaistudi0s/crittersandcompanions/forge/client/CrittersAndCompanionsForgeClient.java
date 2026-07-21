package io.github.bonsaistudi0s.crittersandcompanions.forge.client;

import io.github.bonsaistudi0s.crittersandcompanions.client.CrittersAndCompanionsClient;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CrittersAndCompanionsForgeClient {

    public static void init(IEventBus modEventBus) {
        CrittersAndCompanionsClient.init();
        modEventBus.addListener(CrittersAndCompanionsForgeClient::onClientSetup);

        //noinspection removal
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> CACCommonConfig.createConfigScreen(parent)
                )
        );
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        CrittersAndCompanionsClient.setup();
    }
}
