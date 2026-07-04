package io.github.bonsaistudi0s.crittersandcompanions.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.client.CrittersAndCompanionsClient;
import io.github.bonsaistudi0s.crittersandcompanions.client.gui.RolyPolyScreen;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACMenuTypes;

@Mod(value = CrittersAndCompanions.MODID, dist = Dist.CLIENT)
public class CrittersAndCompanionsNeoForgeClient {

    public CrittersAndCompanionsNeoForgeClient(IEventBus modEventBus) {
        CrittersAndCompanionsClient.init();
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerScreens);

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> CACCommonConfig.createConfigScreen(parent)
        );
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        CrittersAndCompanionsClient.setup();
    }

    // https://github.com/architectury/architectury-api/issues/641
    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(CACMenuTypes.ROLY_POLY_CHEST.get(), RolyPolyScreen::new);
    }
}
