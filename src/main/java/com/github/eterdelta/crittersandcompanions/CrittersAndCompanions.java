package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.DragonflyRenderer;
import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.KoiFishRenderer;
import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.OtterRenderer;
import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.SeaBunnyRenderer;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import com.github.eterdelta.crittersandcompanions.entity.KoiFishEntity;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import com.github.eterdelta.crittersandcompanions.entity.SeaBunnyEntity;
import com.github.eterdelta.crittersandcompanions.handler.SpawnHandler;
import com.github.eterdelta.crittersandcompanions.registry.CaCEntities;
import com.github.eterdelta.crittersandcompanions.registry.CaCItems;
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
        event.enqueueWork(SpawnHandler::registerSpawnPlacements);
    }

    private void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(CaCEntities.OTTER.get(), OtterEntity.createAttributes().build());
        event.put(CaCEntities.KOI_FISH.get(), KoiFishEntity.createAttributes().build());
        event.put(CaCEntities.DRAGONFLY.get(), DragonflyEntity.createAttributes().build());
        event.put(CaCEntities.SEA_BUNNY.get(), SeaBunnyEntity.createAttributes().build());
    }

    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CaCEntities.OTTER.get(), OtterRenderer::new);
        event.registerEntityRenderer(CaCEntities.KOI_FISH.get(), KoiFishRenderer::new);
        event.registerEntityRenderer(CaCEntities.DRAGONFLY.get(), DragonflyRenderer::new);
        event.registerEntityRenderer(CaCEntities.SEA_BUNNY.get(), SeaBunnyRenderer::new);
    }
}
