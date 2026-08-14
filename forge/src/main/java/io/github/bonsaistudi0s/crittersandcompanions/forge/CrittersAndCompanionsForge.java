package io.github.bonsaistudi0s.crittersandcompanions.forge;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.resource.PathPackResources;

import dev.architectury.platform.forge.EventBuses;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.handler.PlayerHandler;
import io.github.bonsaistudi0s.crittersandcompanions.forge.client.CrittersAndCompanionsForgeClient;
import io.github.bonsaistudi0s.crittersandcompanions.forge.common.loot.AddItemModifier;
import io.github.bonsaistudi0s.crittersandcompanions.forge.common.loot.ReplaceItemModifier;

@Mod(CrittersAndCompanions.MODID)
public final class CrittersAndCompanionsForge {

    public CrittersAndCompanionsForge() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(CrittersAndCompanions.MODID, modEventBus);

        CrittersAndCompanions.init();

        var lootModifiers = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CrittersAndCompanions.MODID);
        lootModifiers.register("replace_item", () -> ReplaceItemModifier.CODEC);
        lootModifiers.register("add_item", () -> AddItemModifier.CODEC);
        lootModifiers.register(modEventBus);

        if (FMLLoader.getDist() == Dist.CLIENT) {
            CrittersAndCompanionsForgeClient.init(modEventBus);
        }
    }

    @Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onAddPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                var modFile = ModList.get().getModFileById(CrittersAndCompanions.MODID).getFile();
                var resourcePath = modFile.findResource("resourcepacks/friendlyart");
                try (var pack = new PathPackResources(modFile.getFileName() + ":" + resourcePath, true, resourcePath)) {
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
        public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
            PlayerHandler.onPlayerStartTracking(event.getTarget(), event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerStopTracking(PlayerEvent.StopTracking event) {
            PlayerHandler.onPlayerStopTracking(event.getTarget(), event.getEntity());
        }
    }
}
