package earth.terrarium.crittersandcompanions.fabric;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import earth.terrarium.crittersandcompanions.client.model.BubbleModel;
import earth.terrarium.crittersandcompanions.client.model.GrapplingHookModel;
import earth.terrarium.crittersandcompanions.client.renderer.BubbleLayer;
import earth.terrarium.crittersandcompanions.client.renderer.GrapplingHookRenderer;
import earth.terrarium.crittersandcompanions.client.renderer.geo.entity.*;
import earth.terrarium.crittersandcompanions.common.entity.*;
import earth.terrarium.crittersandcompanions.common.registry.ModEntities;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CACClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerEntityRenderers();
        registerAttributes();
        registerEntityLayers();

        ItemProperties.register(ModItems.DUMBO_OCTOPUS_BUCKET.get(), new ResourceLocation("variant"), (stack, clientLevel, entity, seed) -> {
            if (stack.getTag() != null && stack.getTag().contains("BucketVariant")) {
                return stack.getTag().getInt("BucketVariant");
            } else {
                return 0.0F;
            }
        });
        ItemProperties.register(ModItems.SEA_BUNNY_BUCKET.get(), new ResourceLocation("variant"), (stack, clientLevel, entity, seed) -> {
            if (stack.getTag() != null && stack.getTag().contains("BucketVariant")) {
                return stack.getTag().getInt("BucketVariant");
            } else {
                return 0.0F;
            }
        });
    }

    private void registerAttributes() {
        EntityAttributeRegistry.register(ModEntities.OTTER, OtterEntity::createAttributes);
        EntityAttributeRegistry.register(ModEntities.JUMPING_SPIDER, JumpingSpiderEntity::createAttributes);
        EntityAttributeRegistry.register(ModEntities.KOI_FISH, KoiFishEntity::createAttributes);
        EntityAttributeRegistry.register(ModEntities.DRAGONFLY, DragonflyEntity::createAttributes);
        EntityAttributeRegistry.register(ModEntities.SEA_BUNNY, SeaBunnyEntity::createAttributes);
        EntityAttributeRegistry.register(ModEntities.SHIMA_ENAGA, ShimaEnagaEntity::createAttributes);
        EntityAttributeRegistry.register(ModEntities.FERRET, FerretEntity::createAttributes);
        EntityAttributeRegistry.register(ModEntities.DUMBO_OCTOPUS, DumboOctopusEntity::createAttributes);
        EntityAttributeRegistry.register(ModEntities.LEAF_INSECT, LeafInsectEntity::createAttributes);
        EntityAttributeRegistry.register(ModEntities.RED_PANDA, RedPandaEntity::createAttributes);
    }

    public void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntities.OTTER.get(), OtterRenderer::new);
        EntityRendererRegistry.register(ModEntities.JUMPING_SPIDER.get(), JumpingSpiderRenderer::new);
        EntityRendererRegistry.register(ModEntities.KOI_FISH.get(), KoiFishRenderer::new);
        EntityRendererRegistry.register(ModEntities.DRAGONFLY.get(), DragonflyRenderer::new);
        EntityRendererRegistry.register(ModEntities.SEA_BUNNY.get(), SeaBunnyRenderer::new);
        EntityRendererRegistry.register(ModEntities.SHIMA_ENAGA.get(), ShimaEnagaRenderer::new);
        EntityRendererRegistry.register(ModEntities.FERRET.get(), FerretRenderer::new);
        EntityRendererRegistry.register(ModEntities.GRAPPLING_HOOK.get(), GrapplingHookRenderer::new);
        EntityRendererRegistry.register(ModEntities.DUMBO_OCTOPUS.get(), DumboOctopusRenderer::new);
        EntityRendererRegistry.register(ModEntities.LEAF_INSECT.get(), LeafInsectRenderer::new);
        EntityRendererRegistry.register(ModEntities.RED_PANDA.get(), RedPandaRenderer::new);
    }

    public void registerEntityLayers() {
        EntityModelLayerRegistry.registerModelLayer(BubbleLayer.LAYER_LOCATION, BubbleModel::createLayer);
        EntityModelLayerRegistry.registerModelLayer(GrapplingHookRenderer.LAYER_LOCATION, GrapplingHookModel::createLayer);
    }
}
