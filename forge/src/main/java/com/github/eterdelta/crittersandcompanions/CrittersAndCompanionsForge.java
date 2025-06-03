package com.github.eterdelta.crittersandcompanions;

import static com.github.eterdelta.crittersandcompanions.CrittersAndCompanions.MODID;

import com.github.eterdelta.crittersandcompanions.handler.PlayerHandler;
import java.nio.file.Path;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.context.UseOnContext;
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
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.resource.PathPackResources;

@Mod(MODID)
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CrittersAndCompanionsForge {

    public CrittersAndCompanionsForge() {
        CrittersAndCompanions.init();

        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(CrittersAndCompanions::setup));
        modBus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(CrittersAndCompanionsClient::clientSetup));

        var lootModifiers = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);
        lootModifiers.register("replace_item", () -> ReplaceItemModifier.CODEC);
        lootModifiers.register(modBus);
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFile modFile = ModList.get().getModFileById(MODID).getFile();
            Path resourcePath = modFile.findResource("resourcepacks/friendlyart");
            try (PathPackResources pack = new PathPackResources(modFile.getFileName() + ":" + resourcePath, true, resourcePath)) {
                event.addRepositorySource(consumer -> consumer.accept(Pack.readMetaAndCreate(
                        "builtin/" + MODID,
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

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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