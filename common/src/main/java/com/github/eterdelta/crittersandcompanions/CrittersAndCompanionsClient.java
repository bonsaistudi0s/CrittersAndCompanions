package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.client.model.BubbleModel;
import com.github.eterdelta.crittersandcompanions.client.model.GrapplingHookModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.DragonflyModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.DumboOctopusModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.FerretModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.JumpingSpiderModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.KoiFishModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.LeafInsectModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.RedPandaModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.SeaBunnyModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.ShimaEnagaModel;
import com.github.eterdelta.crittersandcompanions.client.renderer.BubbleLayer;
import com.github.eterdelta.crittersandcompanions.client.renderer.GrapplingHookRenderer;
import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.OtterRenderer;
import com.github.eterdelta.crittersandcompanions.mixin.ItemPropertiesAccessor;
import com.github.eterdelta.crittersandcompanions.platform.event.RegisterEntityRenderers;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CrittersAndCompanionsClient {
    public static void clientSetup() {
        var tagKey = "BucketVariant";
        ItemPropertiesAccessor.invokeRegister(CACItems.DUMBO_OCTOPUS_BUCKET.get(), new ResourceLocation("variant"), (stack, clientLevel, entity, seed) -> {
            if (stack.getTag() != null && stack.getTag().contains(tagKey)) {
                return stack.getTag().getInt(tagKey);
            } else {
                return 0.0F;
            }
        });
        ItemPropertiesAccessor.invokeRegister(CACItems.SEA_BUNNY_BUCKET.get(), new ResourceLocation("variant"), (stack, clientLevel, entity, seed) -> {
            if (stack.getTag() != null && stack.getTag().contains(tagKey)) {
                return stack.getTag().getInt(tagKey);
            } else {
                return 0.0F;
            }
        });
    }

    public static void registerEntityRenderers(RegisterEntityRenderers event) {
        event.accept(CACEntities.OTTER.get(), OtterRenderer::new);
        event.accept(CACEntities.JUMPING_SPIDER.get(), context -> new GeoEntityRenderer<>(context, new JumpingSpiderModel()));
        event.accept(CACEntities.KOI_FISH.get(), context -> new GeoEntityRenderer<>(context, new KoiFishModel()));
        event.accept(CACEntities.DRAGONFLY.get(), context -> new GeoEntityRenderer<>(context, new DragonflyModel()));
        event.accept(CACEntities.SEA_BUNNY.get(), context -> new GeoEntityRenderer<>(context, new SeaBunnyModel()));
        event.accept(CACEntities.SHIMA_ENAGA.get(), context -> new GeoEntityRenderer<>(context, new ShimaEnagaModel()));
        event.accept(CACEntities.FERRET.get(), context -> new GeoEntityRenderer<>(context, new FerretModel()));
        event.accept(CACEntities.GRAPPLING_HOOK.get(), GrapplingHookRenderer::new);
        event.accept(CACEntities.DUMBO_OCTOPUS.get(), context -> new GeoEntityRenderer<>(context, new DumboOctopusModel()));
        event.accept(CACEntities.LEAF_INSECT.get(), context -> new GeoEntityRenderer<>(context, new LeafInsectModel()));
        event.accept(CACEntities.RED_PANDA.get(), context -> new GeoEntityRenderer<>(context, new RedPandaModel()));
    }

    public static void registerEntityLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> event) {
        event.accept(BubbleLayer.LAYER_LOCATION, BubbleModel::createLayer);
        event.accept(GrapplingHookRenderer.LAYER_LOCATION, GrapplingHookModel::createLayer);
    }
}
