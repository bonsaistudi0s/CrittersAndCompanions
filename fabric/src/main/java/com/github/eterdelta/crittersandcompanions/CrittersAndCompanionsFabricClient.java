package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.client.renderer.BubbleLayer;
import com.github.eterdelta.crittersandcompanions.client.renderer.SilkLeashRenderer;
import com.github.eterdelta.crittersandcompanions.registry.CACBlocks;
import com.github.eterdelta.crittersandcompanions.client.gui.RolyPolyScreen;
import com.github.eterdelta.crittersandcompanions.registry.CACMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.event.GeoRenderEvent;

public class CrittersAndCompanionsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CrittersAndCompanionsClient.init();

        MenuScreens.register(CACMenuTypes.ROLY_POLY_CHEST.get(), RolyPolyScreen::new);

        GeoRenderEvent.Entity.Post.EVENT.register(event ->
                SilkLeashRenderer.renderSilkLeash(event.getEntity(), event.getPartialTick(), event.getPoseStack(), event.getBufferSource())
        );

        CrittersAndCompanionsClient.registerEntityLayers((id, factory) -> EntityModelLayerRegistry.registerModelLayer(id, factory::get));
        CrittersAndCompanionsClient.registerEntityRenderers(EntityRendererRegistry::register);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, registrationHelper, context) -> {
            if (type != EntityType.PLAYER) return;

            @SuppressWarnings("unchecked")
            var playerRenderer = (RenderLayerParent<Player, PlayerModel<Player>>) renderer;
            registrationHelper.register(new BubbleLayer(playerRenderer, context.getModelSet()));
        });

        var resourcePack = CrittersAndCompanions.createId("friendlyart");
        FabricLoader.getInstance().getModContainer(resourcePack.getNamespace()).ifPresent(mod -> {
            ResourceManagerHelper.registerBuiltinResourcePack(resourcePack, mod, ResourcePackActivationType.NORMAL);
        });

        BlockRenderLayerMap.INSTANCE.putBlock(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(CACBlocks.SILK_COCOON.get(), RenderType.cutout());
    }

}
