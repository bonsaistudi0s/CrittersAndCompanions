package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.client.model.BubbleModel;
import com.github.eterdelta.crittersandcompanions.client.model.GrapplingHookModel;
import com.github.eterdelta.crittersandcompanions.client.model.geo.*;
import com.github.eterdelta.crittersandcompanions.client.renderer.BubbleLayer;
import com.github.eterdelta.crittersandcompanions.client.renderer.GrapplingHookRenderer;
import com.github.eterdelta.crittersandcompanions.client.renderer.SilkLeashRenderer;
import com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity.*;
import com.github.eterdelta.crittersandcompanions.entity.*;
import com.github.eterdelta.crittersandcompanions.handler.SpawnHandler;
import com.github.eterdelta.crittersandcompanions.network.CACPacketHandler;
import com.github.eterdelta.crittersandcompanions.registry.CACBlocks;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.event.GeoRenderEvent;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.function.Supplier;

@Mod(CrittersAndCompanions.MODID)
public class CrittersAndCompanions {
    public static final String MODID = "crittersandcompanions";

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("main", CreativeModeTab.builder().icon(() -> CACItems.PEARL_NECKLACE_1.get().getDefaultInstance())::build);

    public CrittersAndCompanions() {
        GeckoLib.initialize();
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CACBlocks.BLOCKS.register(eventBus);
        CACEntities.ENTITIES.register(eventBus);
        CACItems.ITEMS.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        CACSounds.SOUNDS.register(eventBus);

        eventBus.addListener(this::onSetup);
        eventBus.addListener(this::onAttributeCreation);
        eventBus.addListener(this::registerEntityRenderers);
        eventBus.addListener(this::registerEntityLayers);
        if (FMLEnvironment.dist.isClient()) {
            eventBus.addListener(this::addEntityLayers);
            MinecraftForge.EVENT_BUS.addListener(SilkLeashRenderer::renderSilkLeash);
        }

        eventBus.addListener(this::gatherData);
        eventBus.addListener(this::addItemsToTab);
        //eventBus.addListener(this::onAddPackFinders);
    }

    public void gatherData(GatherDataEvent event){
        SpawnHandler.datagenBiomeModifiers(event);
    }

    /*
    public void onAddPackFinders(AddPackFindersEvent event) {
        try {
            System.out.println("Hello");
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                IModFile modFile = ModList.get().getModFileById(MODID).getFile();
                Path resourcePath = modFile.findResource("builtin/friendlyart");
                PathPackResources pack = new PathPackResources(modFile.getFileName() + ":" + resourcePath, resourcePath);
                PackMetadataSection metadataSection = pack.getMetadataSection(PackMetadataSection.SERIALIZER);
                if (metadataSection != null) {
                    event.addRepositorySource((packConsumer, packConstructor) ->
                            packConsumer.accept(packConstructor.create(
                                    "builtin/" + MODID, Component.literal("Friendly Critter Art"), false,
                                    () -> pack, metadataSection, Pack.Position.BOTTOM, PackSource.BUILT_IN, false)));
                }
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
     */

    public void onSetup(FMLCommonSetupEvent event) {
        CACPacketHandler.registerPackets();
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
        event.put(CACEntities.JUMPING_SPIDER.get(), JumpingSpiderEntity.createAttributes().build());
        event.put(CACEntities.KOI_FISH.get(), KoiFishEntity.createAttributes().build());
        event.put(CACEntities.DRAGONFLY.get(), DragonflyEntity.createAttributes().build());
        event.put(CACEntities.SEA_BUNNY.get(), SeaBunnyEntity.createAttributes().build());
        event.put(CACEntities.SHIMA_ENAGA.get(), ShimaEnagaEntity.createAttributes().build());
        event.put(CACEntities.FERRET.get(), FerretEntity.createAttributes().build());
        event.put(CACEntities.DUMBO_OCTOPUS.get(), DumboOctopusEntity.createAttributes().build());
        event.put(CACEntities.LEAF_INSECT.get(), LeafInsectEntity.createAttributes().build());
        event.put(CACEntities.RED_PANDA.get(), RedPandaEntity.createAttributes().build());
    }

    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CACEntities.OTTER.get(), OtterRenderer::new);
        event.registerEntityRenderer(CACEntities.JUMPING_SPIDER.get(), context -> new GeoEntityRenderer<>(context, new JumpingSpiderModel()));
        event.registerEntityRenderer(CACEntities.KOI_FISH.get(), context -> new GeoEntityRenderer<>(context, new KoiFishModel()));
        event.registerEntityRenderer(CACEntities.DRAGONFLY.get(), context -> new GeoEntityRenderer<>(context, new DragonflyModel()));
        event.registerEntityRenderer(CACEntities.SEA_BUNNY.get(), context -> new GeoEntityRenderer<>(context, new SeaBunnyModel()));
        event.registerEntityRenderer(CACEntities.SHIMA_ENAGA.get(), context -> new GeoEntityRenderer<>(context, new ShimaEnagaModel()));
        event.registerEntityRenderer(CACEntities.FERRET.get(), context -> new GeoEntityRenderer<>(context, new FerretModel()));
        event.registerEntityRenderer(CACEntities.GRAPPLING_HOOK.get(), GrapplingHookRenderer::new);
        event.registerEntityRenderer(CACEntities.DUMBO_OCTOPUS.get(), context -> new GeoEntityRenderer<>(context, new DumboOctopusModel()));
        event.registerEntityRenderer(CACEntities.LEAF_INSECT.get(), context -> new GeoEntityRenderer<>(context, new LeafInsectModel()));
        event.registerEntityRenderer(CACEntities.RED_PANDA.get(), context -> new GeoEntityRenderer<>(context, new RedPandaModel()));
    }

    public void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BubbleLayer.LAYER_LOCATION, BubbleModel::createLayer);
        event.registerLayerDefinition(GrapplingHookRenderer.LAYER_LOCATION, GrapplingHookModel::createLayer);
    }

    @OnlyIn(Dist.CLIENT)
    public void addEntityLayers(EntityRenderersEvent.AddLayers event) {
        if (FMLEnvironment.dist.isClient()) {
            for (String skinName : event.getSkins()) {
                LivingEntityRenderer<Player, PlayerModel<Player>> skinRenderer = event.getSkin(skinName);
                skinRenderer.addLayer(new BubbleLayer(skinRenderer, event.getEntityModels()));
            }
        }
    }

    public void addItemsToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CREATIVE_TAB.get()) {
            CACItems.ITEMS.getEntries().stream().map(Supplier::get).forEach(event::accept);
        }
    }
}
