package earth.terrarium.crittersandcompanions.client;

import earth.terrarium.crittersandcompanions.client.model.BubbleModel;
import earth.terrarium.crittersandcompanions.client.model.GrapplingHookModel;
import earth.terrarium.crittersandcompanions.client.renderer.BubbleLayer;
import earth.terrarium.crittersandcompanions.client.renderer.GrapplingHookRenderer;
import earth.terrarium.crittersandcompanions.client.renderer.geo.entity.*;
import earth.terrarium.crittersandcompanions.common.registry.ModEntities;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CrittersAndCompanionsClient {
    public static  <T extends Entity> void registerEntityRenderers(RendererRegistry registry) {
    /*
        registry.register(ModEntities.OTTER.get(), OtterRenderer::new);
        registry.register(ModEntities.JUMPING_SPIDER.get(), JumpingSpiderRenderer::new);
        registry.register(ModEntities.KOI_FISH.get(), KoiFishRenderer::new);
        registry.register(ModEntities.DRAGONFLY.get(), DragonflyRenderer::new);
        registry.register(ModEntities.SEA_BUNNY.get(), SeaBunnyRenderer::new);
        registry.register(ModEntities.SHIMA_ENAGA.get(), ShimaEnagaRenderer::new);
        registry.register(ModEntities.FERRET.get(), FerretRenderer::new);
        registry.register(ModEntities.GRAPPLING_HOOK.get(), GrapplingHookRenderer::new);
        registry.register(ModEntities.DUMBO_OCTOPUS.get(), DumboOctopusRenderer::new);
        registry.register(ModEntities.LEAF_INSECT.get(), LeafInsectRenderer::new);
        registry.register(ModEntities.RED_PANDA.get(), RedPandaRenderer::new);
     */
    }

    public static void registerEntityLayer(LayerDefinitionRegistry event) {
        event.register(BubbleLayer.LAYER_LOCATION, BubbleModel::createLayer);
        event.register(GrapplingHookRenderer.LAYER_LOCATION, GrapplingHookModel::createLayer);
    }

    public static void registerItemProperties(ItemPropertyRegistry registry) {
    /*
        registry.register(ModItems.DUMBO_OCTOPUS_BUCKET.get(), new ResourceLocation("variant"), (stack, clientLevel, entity, seed) -> {
            if (stack.getTag() != null && stack.getTag().contains("BucketVariant")) {
                return stack.getTag().getInt("BucketVariant");
            } else {
                return 0.0F;
            }
        });
        registry.register(ModItems.SEA_BUNNY_BUCKET.get(), new ResourceLocation("variant"), (stack, clientLevel, entity, seed) -> {
            if (stack.getTag() != null && stack.getTag().contains("BucketVariant")) {
                return stack.getTag().getInt("BucketVariant");
            } else {
                return 0.0F;
            }
        });
     */
    }

    public interface RendererRegistry {
        <T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> rendererProvider);
    }

    public interface LayerDefinitionRegistry {
        void register(ModelLayerLocation location, Supplier<LayerDefinition> definition);
    }

    public interface ItemPropertyRegistry {
        void register(Item item, ResourceLocation name, ClampedItemPropertyFunction property);
    }
}
