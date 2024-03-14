package earth.terrarium.crittersandcompanions.forge;

import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.client.model.BubbleModel;
import earth.terrarium.crittersandcompanions.client.model.GrapplingHookModel;
import earth.terrarium.crittersandcompanions.client.renderer.BubbleLayer;
import earth.terrarium.crittersandcompanions.client.renderer.GrapplingHookRenderer;
import earth.terrarium.crittersandcompanions.client.renderer.geo.entity.*;
import earth.terrarium.crittersandcompanions.common.entity.*;
import earth.terrarium.crittersandcompanions.common.handler.PlayerHandler;
import earth.terrarium.crittersandcompanions.common.handler.SpawnHandler;
import earth.terrarium.crittersandcompanions.common.network.NetworkHandler;
import earth.terrarium.crittersandcompanions.common.registry.ModEntities;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.geckolib.GeckoLib;

import java.util.function.Supplier;

@Mod(CrittersAndCompanions.MODID)
public class CrittersAndCompanionsForge {

    public CrittersAndCompanionsForge() {
        GeckoLib.initialize();
        CrittersAndCompanions.init();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::onSetup);
        eventBus.addListener(this::onAttributeCreation);
        eventBus.addListener(this::registerEntityRenderers);
        eventBus.addListener(this::registerEntityLayers);
        eventBus.addListener(this::addItemsToTab);
        if (FMLEnvironment.dist.isClient()) {
            eventBus.addListener(this::addEntityLayers);
        }

        forgeBus.addListener(this::onPlayerTick);
        forgeBus.addListener(this::onPlayerEntityInteract);
        forgeBus.addListener(this::startTrackingPlayer);
        forgeBus.addListener(this::stopTrackingPlayer);

        // eventBus.addListener(this::gatherData);
        // eventBus.addListener(this::onAddPackFinders);
    }

    /*
    public void gatherData(GatherDataEvent event) {
        SpawnHandler.datagenBiomeModifiers(event);
    }

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
        NetworkHandler.registerPackets();
        event.enqueueWork(() -> {
            SpawnHandler.registerSpawnPlacements();
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
        });
    }

    private void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.OTTER.get(), OtterEntity.createAttributes().build());
        event.put(ModEntities.JUMPING_SPIDER.get(), JumpingSpiderEntity.createAttributes().build());
        event.put(ModEntities.KOI_FISH.get(), KoiFishEntity.createAttributes().build());
        event.put(ModEntities.DRAGONFLY.get(), DragonflyEntity.createAttributes().build());
        event.put(ModEntities.SEA_BUNNY.get(), SeaBunnyEntity.createAttributes().build());
        event.put(ModEntities.SHIMA_ENAGA.get(), ShimaEnagaEntity.createAttributes().build());
        event.put(ModEntities.FERRET.get(), FerretEntity.createAttributes().build());
        event.put(ModEntities.DUMBO_OCTOPUS.get(), DumboOctopusEntity.createAttributes().build());
        event.put(ModEntities.LEAF_INSECT.get(), LeafInsectEntity.createAttributes().build());
        event.put(ModEntities.RED_PANDA.get(), RedPandaEntity.createAttributes().build());
    }

    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.OTTER.get(), OtterRenderer::new);
        event.registerEntityRenderer(ModEntities.JUMPING_SPIDER.get(), JumpingSpiderRenderer::new);
        event.registerEntityRenderer(ModEntities.KOI_FISH.get(), KoiFishRenderer::new);
        event.registerEntityRenderer(ModEntities.DRAGONFLY.get(), DragonflyRenderer::new);
        event.registerEntityRenderer(ModEntities.SEA_BUNNY.get(), SeaBunnyRenderer::new);
        event.registerEntityRenderer(ModEntities.SHIMA_ENAGA.get(), ShimaEnagaRenderer::new);
        event.registerEntityRenderer(ModEntities.FERRET.get(), FerretRenderer::new);
        event.registerEntityRenderer(ModEntities.GRAPPLING_HOOK.get(), GrapplingHookRenderer::new);
        event.registerEntityRenderer(ModEntities.DUMBO_OCTOPUS.get(), DumboOctopusRenderer::new);
        event.registerEntityRenderer(ModEntities.LEAF_INSECT.get(), LeafInsectRenderer::new);
        event.registerEntityRenderer(ModEntities.RED_PANDA.get(), RedPandaRenderer::new);
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

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PlayerHandler.onPlayerTick(event.player);
        }
    }

    public void onPlayerEntityInteract(PlayerInteractEvent.EntityInteract event) {
        PlayerHandler.onPlayerEntityInteract(event.getEntity(), event.getHand(), event.getTarget(), event::setCanceled, event::setCancellationResult);
    }

    public void startTrackingPlayer(PlayerEvent.StartTracking event) {
        PlayerHandler.onPlayerStartTracking(event.getEntity(), event.getTarget());
    }

    public void stopTrackingPlayer(PlayerEvent.StopTracking event) {
        PlayerHandler.onPlayerStopTracking(event.getEntity(), event.getTarget());
    }

    public void addItemsToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ModItems.TAB.get()) {
            ModItems.ITEMS.getEntries().stream().map(Supplier::get).forEach(event::accept);
        }
    }
}
