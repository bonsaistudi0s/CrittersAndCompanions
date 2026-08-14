package io.github.bonsaistudi0s.crittersandcompanions.neoforge.client;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.player.Player;
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
import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.BubbleLayer;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACMenuTypes;
import net.minecraft.client.model.PlayerModel;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = CrittersAndCompanions.MODID, dist = Dist.CLIENT)
public class CrittersAndCompanionsNeoForgeClient {

    public CrittersAndCompanionsNeoForgeClient(IEventBus modEventBus) {
        CrittersAndCompanionsClient.init();
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerScreens);
        modEventBus.addListener(this::addLayers);

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> CACCommonConfig.createConfigScreen(parent)
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addLayers(EntityRenderersEvent.AddLayers event) {
        for (var skin : event.getSkins()) {
            var renderer = event.getSkin(skin);
            if (renderer != null) {
                var playerRenderer = (RenderLayerParent<Player, PlayerModel<Player>>) renderer;
                ((LivingEntityRenderer) renderer).addLayer(new BubbleLayer(playerRenderer, event.getEntityModels()));
            }
        }
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        CrittersAndCompanionsClient.setup();
    }

    // https://github.com/architectury/architectury-api/issues/641
    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(CACMenuTypes.ROLY_POLY_CHEST.get(), RolyPolyScreen::new);
    }
}
