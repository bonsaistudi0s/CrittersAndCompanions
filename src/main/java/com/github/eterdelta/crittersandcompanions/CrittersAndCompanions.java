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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.resource.PathPackResources;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.nio.file.Path;
import java.util.function.Supplier;

@Mod(CrittersAndCompanions.MODID)
public class CrittersAndCompanions {
    public static final String MODID = "crittersandcompanions";

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("main", CreativeModeTab.builder()
            .icon(() -> CACItems.PEARL_NECKLACE_1.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.crittersandcompanions"))
            ::build);

    public CrittersAndCompanions() {
        GeckoLib.initialize();
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.register(this);

        CACBlocks.BLOCKS.register(eventBus);
        CACEntities.ENTITIES.register(eventBus);
        CACItems.ITEMS.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        CACSounds.SOUNDS.register(eventBus);

        if (FMLEnvironment.dist.isClient()) {
            MinecraftForge.EVENT_BUS.addListener(SilkLeashRenderer::renderSilkLeash);
        }
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent event){
        SpawnHandler.datagenBiomeModifiers(event);
    }

    @SubscribeEvent
    public void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFile modFile = ModList.get().getModFileById(MODID).getFile();
            Path resourcePath = modFile.findResource("builtin/friendlyart");
            try (PathPackResources pack = new PathPackResources(modFile.getFileName() + ":" + resourcePath, true, resourcePath)) {
                event.addRepositorySource(consumer -> consumer.accept(Pack.readMetaAndCreate(
                        "builtin/" + MODID,
                        Component.literal("Friendly Critter Art"),
                        false,
                        (ignored) -> pack,
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.BOTTOM,
                        PackSource.BUILT_IN)));
            }
        }
    }

    @SubscribeEvent
    public void onSetup(FMLCommonSetupEvent event) {
        CACPacketHandler.registerPackets();
        event.enqueueWork(SpawnHandler::registerSpawnPlacements);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
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

    @SubscribeEvent
    public void onAttributeCreation(EntityAttributeCreationEvent event) {
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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BubbleLayer.LAYER_LOCATION, BubbleModel::createLayer);
        event.registerLayerDefinition(GrapplingHookRenderer.LAYER_LOCATION, GrapplingHookModel::createLayer);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void addEntityLayers(EntityRenderersEvent.AddLayers event) {
        if (FMLEnvironment.dist.isClient()) {
            for (String skinName : event.getSkins()) {
                LivingEntityRenderer<Player, PlayerModel<Player>> skinRenderer = event.getSkin(skinName);
                skinRenderer.addLayer(new BubbleLayer(skinRenderer, event.getEntityModels()));
            }
        }
    }

    @SubscribeEvent
    public void addItemsToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CREATIVE_TAB.get()) {
            CACItems.ITEMS.getEntries().stream().map(Supplier::get).forEach(event::accept);
        }
    }
}
