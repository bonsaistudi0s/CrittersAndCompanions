package io.github.bonsaistudi0s.crittersandcompanions.neoforge;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.handler.PlayerHandler;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACPotions;
import io.github.bonsaistudi0s.crittersandcompanions.neoforge.common.loot.AddItemModifier;
import io.github.bonsaistudi0s.crittersandcompanions.neoforge.common.loot.ReplaceItemModifier;
import io.github.bonsaistudi0s.crittersandcompanions.neoforge.common.world.CACSpawnsBiomeModifier;

@Mod(CrittersAndCompanions.MODID)
@EventBusSubscriber(modid = CrittersAndCompanions.MODID)
public final class CrittersAndCompanionsNeoForge {

    public CrittersAndCompanionsNeoForge(ModContainer container, IEventBus modBus) {
        CrittersAndCompanions.init();

        var lootModifiers = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CrittersAndCompanions.MODID);
        lootModifiers.register("replace_item", () -> ReplaceItemModifier.CODEC);
        lootModifiers.register("add_item", () -> AddItemModifier.CODEC);
        lootModifiers.register(modBus);

        var biomeModifiers = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, CrittersAndCompanions.MODID);
        biomeModifiers.register("config_driven_spawns", () -> CACSpawnsBiomeModifier.CODEC);
        biomeModifiers.register(modBus);
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(CrittersAndCompanions.MODID, "resourcepacks/friendlyart"),
                PackType.CLIENT_RESOURCES,
                Component.literal("Friendly Critter Art"),
                PackSource.BUILT_IN,
                false,
                Pack.Position.BOTTOM
        );
    }

    @EventBusSubscriber(modid = CrittersAndCompanions.MODID)
    public static class ForgeEvents {

        @SubscribeEvent
        public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
            PlayerHandler.onPlayerStartTracking(event.getTarget(), event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerStopTracking(PlayerEvent.StopTracking event) {
            PlayerHandler.onPlayerStopTracking(event.getTarget(), event.getEntity());
        }

        @SubscribeEvent
        public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
            var builder = event.getBuilder();
            CACPotions.registerBrewingRecipes(builder::addMix);
        }
    }
}
