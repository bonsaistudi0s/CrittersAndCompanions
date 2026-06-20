package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.client.model.BubbleModel;
import com.github.eterdelta.crittersandcompanions.client.model.GrapplingHookModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.RolyPolyModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.DragonflyModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.FerretModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.JumpingSpiderModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.LeafInsectModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.OtterModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.RedPandaModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.SeaBunnyModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.ShimaEnagaModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.SnailModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.VariantGeoModel;
import com.github.eterdelta.crittersandcompanions.client.renderer.BubbleLayer;
import com.github.eterdelta.crittersandcompanions.client.renderer.GrapplingHookRenderer;
import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.FerretRenderer;
import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.OtterRenderer;
import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.RolyPolyRenderer;
import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.SnailRenderer;
import com.github.eterdelta.crittersandcompanions.mixin.ItemPropertiesAccessor;
import com.github.eterdelta.crittersandcompanions.platform.event.RegisterEntityRenderers;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

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
        registerProperty(CACItems.DUMBO_OCTOPUS_BUCKET.get(), ResourceLocation.withDefaultNamespace("variant"), BUCKET_VARIANT);
        registerProperty(CACItems.SEA_BUNNY_BUCKET.get(), ResourceLocation.withDefaultNamespace("variant"), BUCKET_VARIANT);
    }

    public static void registerEntityRenderers(RegisterEntityRenderers event) {
        event.accept(CACEntities.OTTER, OtterRenderer::new, OtterModel::new);
        event.accept(CACEntities.JUMPING_SPIDER, GeoEntityRenderer::new, JumpingSpiderModel::new);
        event.accept(CACEntities.KOI_FISH, GeoEntityRenderer::new, VariantGeoModel::new);
        event.accept(CACEntities.DRAGONFLY, GeoEntityRenderer::new, DragonflyModel::new);
        event.accept(CACEntities.SEA_BUNNY, GeoEntityRenderer::new, SeaBunnyModel::new);
        event.accept(CACEntities.SHIMA_ENAGA, GeoEntityRenderer::new, ShimaEnagaModel::new);
        event.accept(CACEntities.FERRET, FerretRenderer::new, FerretModel::new);
        event.accept(CACEntities.GRAPPLING_HOOK, GrapplingHookRenderer::new);
        event.accept(CACEntities.DUMBO_OCTOPUS, GeoEntityRenderer::new, VariantGeoModel::new);
        event.accept(CACEntities.LEAF_INSECT, GeoEntityRenderer::new, LeafInsectModel::new);
        event.accept(CACEntities.RED_PANDA, GeoEntityRenderer::new, RedPandaModel::new);

        event.accept(CACEntities.LADYBUG, GeoEntityRenderer::new, DefaultedEntityGeoModel::new);
        event.accept(CACEntities.STAG_BEETLE, GeoEntityRenderer::new, VariantGeoModel::new);
        event.accept(CACEntities.ROLY_POLY, RolyPolyRenderer::new, RolyPolyModel::new);
        event.accept(CACEntities.SNAIL, SnailRenderer::new, SnailModel::new);
        event.accept(CACEntities.STICK_BUG, GeoEntityRenderer::new, VariantGeoModel::new);
        event.accept(CACEntities.WEEVIL, GeoEntityRenderer::new, DefaultedEntityGeoModel::new);
    }

    public static void registerEntityLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> event) {
        event.accept(BubbleLayer.LAYER_LOCATION, BubbleModel::createLayer);
        event.accept(GrapplingHookRenderer.LAYER_LOCATION, GrapplingHookModel::createLayer);
    }
}
