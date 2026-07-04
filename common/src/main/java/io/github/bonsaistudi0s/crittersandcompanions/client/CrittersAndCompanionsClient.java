package io.github.bonsaistudi0s.crittersandcompanions.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import io.github.bonsaistudi0s.crittersandcompanions.client.gui.RolyPolyScreen;
import io.github.bonsaistudi0s.crittersandcompanions.client.model.BubbleModel;
import io.github.bonsaistudi0s.crittersandcompanions.client.model.GrapplingHookModel;
import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.BubbleLayer;
import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.GrapplingHookRenderer;
import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.mixin.ItemPropertiesAccessor;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACMenuTypes;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class CrittersAndCompanionsClient {

    private static final ItemPropertyFunction BUCKET_VARIANT = (stack, clientLevel, entity, seed) -> {
        var customData = stack.get(DataComponents.BUCKET_ENTITY_DATA);
        if (customData != null) {
            return customData.copyTag().getInt("Variant");
        } else {
            return 0.0F;
        }
    };

    private static void registerProperty(Item item, ResourceLocation id, ItemPropertyFunction function) {
        ItemPropertiesAccessor.getPROPERTIES().computeIfAbsent(item, $ -> new HashMap<>()).put(id, function);
    }

    public static void init() {
        registerEntityRenderers();
        registerEntityLayers();
    }

    public static void setup() {
        registerProperty(CACItems.DUMBO_OCTOPUS_BUCKET.get(), ResourceLocation.withDefaultNamespace("variant"), BUCKET_VARIANT);
        registerProperty(CACItems.SEA_BUNNY_BUCKET.get(), ResourceLocation.withDefaultNamespace("variant"), BUCKET_VARIANT);
        registerMenuScreens();
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(CACEntities.OTTER, OtterRenderer::new);
        EntityRendererRegistry.register(CACEntities.JUMPING_SPIDER, JumpingSpiderRenderer::new);
        EntityRendererRegistry.register(CACEntities.KOI_FISH, KoiFishRenderer::new);
        EntityRendererRegistry.register(CACEntities.DRAGONFLY, DragonflyRenderer::new);
        EntityRendererRegistry.register(CACEntities.SEA_BUNNY, SeaBunnyRenderer::new);
        EntityRendererRegistry.register(CACEntities.SHIMA_ENAGA, ShimaEnagaRenderer::new);
        EntityRendererRegistry.register(CACEntities.FERRET, FerretRenderer::new);
        EntityRendererRegistry.register(CACEntities.DUMBO_OCTOPUS, DumboOctopusRenderer::new);
        EntityRendererRegistry.register(CACEntities.LEAF_INSECT, LeafInsectRenderer::new);
        EntityRendererRegistry.register(CACEntities.RED_PANDA, RedPandaRenderer::new);

        EntityRendererRegistry.register(CACEntities.LADYBUG, LadybugRenderer::new);
        EntityRendererRegistry.register(CACEntities.STAG_BEETLE, StagBeetleRenderer::new);
        EntityRendererRegistry.register(CACEntities.ROLY_POLY, RolyPolyRenderer::new);
        EntityRendererRegistry.register(CACEntities.SNAIL, SnailRenderer::new);
        EntityRendererRegistry.register(CACEntities.STICK_BUG, StickBugRenderer::new);
        EntityRendererRegistry.register(CACEntities.WEEVIL, WeevilRenderer::new);

        EntityRendererRegistry.register(CACEntities.GRAPPLING_HOOK, GrapplingHookRenderer::new);
        EntityRendererRegistry.register(CACEntities.MUD_BALL, context -> new GeoEntityRenderer<>(context, new DefaultedEntityGeoModel<>(BuiltInRegistries.ENTITY_TYPE.getKey(CACEntities.MUD_BALL.get()))));
    }

    private static void registerEntityLayers() {
        EntityModelLayerRegistry.register(BubbleLayer.LAYER_LOCATION, BubbleModel::createLayer);
        EntityModelLayerRegistry.register(GrapplingHookRenderer.LAYER_LOCATION, GrapplingHookModel::createLayer);
    }

    private static void registerMenuScreens() {
        MenuRegistry.registerScreenFactory(CACMenuTypes.ROLY_POLY_CHEST.get(), RolyPolyScreen::new);
    }
}
