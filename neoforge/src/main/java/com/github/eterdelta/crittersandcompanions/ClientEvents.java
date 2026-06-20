package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.client.gui.RolyPolyScreen;
import com.github.eterdelta.crittersandcompanions.client.renderer.BubbleLayer;
import com.github.eterdelta.crittersandcompanions.client.renderer.SilkLeashRenderer;
import com.github.eterdelta.crittersandcompanions.registry.CACMenuTypes;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import software.bernie.geckolib.event.GeoRenderEvent;

@EventBusSubscriber(modid = CrittersAndCompanions.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(CACMenuTypes.ROLY_POLY_CHEST.get(), RolyPolyScreen::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        CrittersAndCompanionsClient.registerEntityLayers(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        CrittersAndCompanionsClient.registerEntityRenderers(event::registerEntityRenderer);
    }

    @SubscribeEvent
    public static void addEntityLayers(EntityRenderersEvent.AddLayers event) {
        for (var skin : event.getSkins()) {
            LivingEntityRenderer<Player, PlayerModel<Player>> skinRenderer = event.getSkin(skin);
            if (skinRenderer != null) {
                skinRenderer.addLayer(new BubbleLayer(skinRenderer, event.getEntityModels()));
            }
        }
    }

    @EventBusSubscriber(modid = CrittersAndCompanions.MODID, value = Dist.CLIENT)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void renderSilkLeash(GeoRenderEvent.Entity.Post event) {
            SilkLeashRenderer.renderSilkLeash(event.getEntity(), event.getPartialTick(), event.getPoseStack(), event.getBufferSource());
        }

    }

}
