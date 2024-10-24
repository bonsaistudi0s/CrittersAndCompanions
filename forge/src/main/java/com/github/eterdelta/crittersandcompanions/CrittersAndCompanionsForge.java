package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.client.renderer.SilkLeashRenderer;
import com.github.eterdelta.crittersandcompanions.handler.PlayerHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;

import java.nio.file.Path;

@Mod(CrittersAndCompanions.MODID)
@Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CrittersAndCompanionsForge {

    public CrittersAndCompanionsForge() {
        CrittersAndCompanions.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLCommonSetupEvent event) -> event.enqueueWork(CrittersAndCompanions::setup));
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent event) -> event.enqueueWork(CrittersAndCompanions::clientSetup));

        if (FMLEnvironment.dist.isClient()) {
            MinecraftForge.EVENT_BUS.addListener(SilkLeashRenderer::renderSilkLeash);
        }
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFile modFile = ModList.get().getModFileById(CrittersAndCompanions.MODID).getFile();
            Path resourcePath = modFile.findResource("resourcepacks/friendlyart");
            try (PathPackResources pack = new PathPackResources(modFile.getFileName() + ":" + resourcePath, true, resourcePath)) {
                event.addRepositorySource(consumer -> consumer.accept(Pack.readMetaAndCreate(
                        "builtin/" + CrittersAndCompanions.MODID,
                        Component.literal("Friendly Critter Art"),
                        false,
                        ignored -> pack,
                        PackType.CLIENT_RESOURCES,
                        Pack.Position.BOTTOM,
                        PackSource.BUILT_IN)));
            }
        }
    }

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        CrittersAndCompanions.onAttributeCreation(event::put);
    }

    @Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onPlayerEntityInteract(PlayerInteractEvent.EntityInteract event) {
            var context = new UseOnContext(event.getEntity(), event.getHand(), null);
            var result = PlayerHandler.onPlayerEntityInteract(event.getTarget(), context);
            if (result != null) {
                event.setCancellationResult(result);
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            PlayerHandler.onPlayerTick(event.player);
        }

        @SubscribeEvent
        public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
            PlayerHandler.onPlayerStartTracking(event.getTarget(), event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerStopTracking(PlayerEvent.StopTracking event) {
            PlayerHandler.onPlayerStopTracking(event.getTarget(), event.getEntity());
        }

    }

}