package io.github.bonsaistudi0s.crittersandcompanions.forge.client;

import io.github.bonsaistudi0s.crittersandcompanions.client.CrittersAndCompanionsClient;
import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.BubbleLayer;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CrittersAndCompanionsForgeClient {

    public static void init(IEventBus modEventBus) {
        CrittersAndCompanionsClient.init();
        modEventBus.addListener(CrittersAndCompanionsForgeClient::onClientSetup);
        modEventBus.addListener(CrittersAndCompanionsForgeClient::addLayers);

        //noinspection removal
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (client, parent) -> CACCommonConfig.createConfigScreen(parent)
                )
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (var skin : event.getSkins()) {
            var renderer = event.getSkin(skin);
            if (renderer != null) {
                var playerRenderer = (RenderLayerParent<Player, PlayerModel<Player>>) renderer;
                ((LivingEntityRenderer) renderer).addLayer(new BubbleLayer(playerRenderer, event.getEntityModels()));
            }
        }
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        CrittersAndCompanionsClient.setup();
    }
}
