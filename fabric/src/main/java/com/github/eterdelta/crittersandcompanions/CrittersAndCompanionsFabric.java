package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.client.renderer.BubbleLayer;
import com.github.eterdelta.crittersandcompanions.client.renderer.SilkLeashRenderer;
import com.github.eterdelta.crittersandcompanions.handler.PlayerHandler;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityInteractCallback;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerTickEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import software.bernie.geckolib.event.GeoRenderEvent;

public class CrittersAndCompanionsFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        CrittersAndCompanions.init();
        CrittersAndCompanions.setup();

        CrittersAndCompanions.onAttributeCreation(FabricDefaultAttributeRegistry::register);

        EntityInteractCallback.EVENT.register((player, hand, target) -> {
            if (player.level().isClientSide()) return null;
            return PlayerHandler.onPlayerEntityInteract(target, new UseOnContext(player, hand, null));
        });

        PlayerTickEvents.END.register(PlayerHandler::onPlayerTick);
        EntityTrackingEvents.START_TRACKING.register(PlayerHandler::onPlayerStartTracking);
        EntityTrackingEvents.STOP_TRACKING.register(PlayerHandler::onPlayerStopTracking);

        CACWorldGen.register();
    }

    @Override
    public void onInitializeClient() {
        CrittersAndCompanions.clientSetup();

        GeoRenderEvent.Entity.Post.EVENT.register(SilkLeashRenderer::renderSilkLeash);

        CrittersAndCompanions.registerEntityLayers((id, factory) -> EntityModelLayerRegistry.registerModelLayer(id, factory::get));
        CrittersAndCompanions.registerEntityRenderers(EntityRendererRegistry::register);

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, registrationHelper, context) -> {
            if (type != EntityType.PLAYER) return;

            @SuppressWarnings("unchecked")
            var playerRenderer = (RenderLayerParent<Player, PlayerModel<Player>>) renderer;
            registrationHelper.register(new BubbleLayer(playerRenderer, context.getModelSet()));
        });

        var resourcePack = new ResourceLocation(CrittersAndCompanions.MODID, "friendlyart");
        FabricLoader.getInstance().getModContainer(resourcePack.getNamespace()).ifPresent(mod -> {
            ResourceManagerHelper.registerBuiltinResourcePack(resourcePack, mod, ResourcePackActivationType.NORMAL);
        });
    }

}
