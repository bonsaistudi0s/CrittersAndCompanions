package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.*;
import com.github.eterdelta.crittersandcompanions.entity.*;
import com.github.eterdelta.crittersandcompanions.handler.SpawnHandler;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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
    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(MODID) {
        public ItemStack makeIcon() {
            return new ItemStack(CACItems.PEARL_NECKLACE_1.get());
        }
    };

    public CrittersAndCompanions() {
        GeckoLib.initialize();
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CACEntities.ENTITIES.register(eventBus);
        CACItems.ITEMS.register(eventBus);
        CACSounds.SOUNDS.register(eventBus);

        eventBus.addListener(this::onSetup);
        eventBus.addListener(this::onAttributeCreation);
        eventBus.addListener(this::registerEntityRenderers);
    }

    public void onSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SpawnHandler.registerSpawnPlacements();
            ItemProperties.register(CACItems.DUMBO_OCTOPUS_BUCKET.get(), new ResourceLocation("variant"), (stack, clientLevel, entity, seed) -> {
                if (stack.getTag() != null && stack.getTag().contains("BucketVariant")) {
                    return stack.getTag().getInt("BucketVariant");
                } else {
                    return 0.0F;
                }
            });
            ItemProperties.register(CACItems.SEA_BUNNY_BUCKET.get(), new ResourceLocation("variant"), (stack, clientLevel, entity, seed) -> {
                if (stack.getTag() != null && stack.getTag().contains("BucketVariant")) {
                    return stack.getTag().getInt("BucketVariant");
                } else {
                    return 0.0F;
                }
            });
        });
    }

    private void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(CACEntities.OTTER.get(), OtterEntity.createAttributes().build());
        event.put(CACEntities.KOI_FISH.get(), KoiFishEntity.createAttributes().build());
        event.put(CACEntities.DRAGONFLY.get(), DragonflyEntity.createAttributes().build());
        event.put(CACEntities.SEA_BUNNY.get(), SeaBunnyEntity.createAttributes().build());
        event.put(CACEntities.FERRET.get(), FerretEntity.createAttributes().build());
        event.put(CACEntities.DUMBO_OCTOPUS.get(), DumboOctopusEntity.createAttributes().build());
        event.put(CACEntities.LEAF_INSECT.get(), LeafInsectEntity.createAttributes().build());
        event.put(CACEntities.RED_PANDA.get(), RedPandaEntity.createAttributes().build());
    }

    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CACEntities.OTTER.get(), OtterRenderer::new);
        event.registerEntityRenderer(CACEntities.KOI_FISH.get(), KoiFishRenderer::new);
        event.registerEntityRenderer(CACEntities.DRAGONFLY.get(), DragonflyRenderer::new);
        event.registerEntityRenderer(CACEntities.SEA_BUNNY.get(), SeaBunnyRenderer::new);
        event.registerEntityRenderer(CACEntities.FERRET.get(), FerretRenderer::new);
        event.registerEntityRenderer(CACEntities.DUMBO_OCTOPUS.get(), DumboOctopusRenderer::new);
        event.registerEntityRenderer(CACEntities.LEAF_INSECT.get(), LeafInsectRenderer::new);
        event.registerEntityRenderer(CACEntities.RED_PANDA.get(), RedPandaRenderer::new);
    }
}
