package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.*;
import com.github.eterdelta.crittersandcompanions.entity.*;
import com.github.eterdelta.crittersandcompanions.handler.SpawnHandler;
import com.github.eterdelta.crittersandcompanions.registry.CaCEntities;
import com.github.eterdelta.crittersandcompanions.registry.CaCItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(CrittersAndCompanions.MODID)
public class CrittersAndCompanions {
    public static final String MODID = "crittersandcompanions";

    public CrittersAndCompanions() {
        GeckoLib.initialize();
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CaCEntities.ENTITIES.register(eventBus);
        CaCItems.ITEMS.register(eventBus);

        eventBus.addListener(this::onSetup);
        eventBus.addListener(this::onAttributeCreation);
        eventBus.addListener(this::registerEntityRenderers);
    }

    public void onSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnHandler.registerSpawnPlacements();
            ItemProperties.register(CaCItems.SEA_BUNNY_BUCKET.get(), new ResourceLocation("variant"), (stack, clientLevel, entity, seed) -> {
                if (stack.getTag() != null && stack.getTag().contains("BucketVariant")) {
                    return stack.getTag().getInt("BucketVariant");
                } else {
                    return 0.0F;
                }
            });
        });
    }

    private void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(CaCEntities.OTTER.get(), OtterEntity.createAttributes().build());
        event.put(CaCEntities.KOI_FISH.get(), KoiFishEntity.createAttributes().build());
        event.put(CaCEntities.DRAGONFLY.get(), DragonflyEntity.createAttributes().build());
        event.put(CaCEntities.SEA_BUNNY.get(), SeaBunnyEntity.createAttributes().build());
        event.put(CaCEntities.FERRET.get(), FerretEntity.createAttributes().build());
    }

    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CaCEntities.OTTER.get(), OtterRenderer::new);
        event.registerEntityRenderer(CaCEntities.KOI_FISH.get(), KoiFishRenderer::new);
        event.registerEntityRenderer(CaCEntities.DRAGONFLY.get(), DragonflyRenderer::new);
        event.registerEntityRenderer(CaCEntities.SEA_BUNNY.get(), SeaBunnyRenderer::new);
        event.registerEntityRenderer(CaCEntities.FERRET.get(), FerretRenderer::new);
    }
}
