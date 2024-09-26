package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.client.renderer.BubbleLayer;
import com.github.eterdelta.crittersandcompanions.client.renderer.SilkLeashRenderer;
import com.github.eterdelta.crittersandcompanions.handler.PlayerHandler;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACTags;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityInteractCallback;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerTickEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import software.bernie.geckolib.event.GeoRenderEvent;

import javax.swing.text.html.HTML;
import java.util.function.Supplier;

public class CrittersAndCompanionsFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        CrittersAndCompanions.init();
        CrittersAndCompanions.setup();

        CrittersAndCompanions.onAttributeCreation(FabricDefaultAttributeRegistry::register);

        EntityInteractCallback.EVENT.register((player, hand, target) ->
                PlayerHandler.onPlayerEntityInteract(target, new UseOnContext(player, hand, null))
        );

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
    }

}
