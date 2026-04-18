package com.github.eterdelta.crittersandcompanions;

import static com.github.eterdelta.crittersandcompanions.CrittersAndCompanions.MODID;

import com.github.eterdelta.crittersandcompanions.handler.PlayerHandler;
import com.github.eterdelta.crittersandcompanions.platform.ForgeConfigs;
import com.github.eterdelta.crittersandcompanions.platform.ForgeNetwork;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(MODID)
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class CrittersAndCompanionsForge {

    public CrittersAndCompanionsForge(ModContainer container, IEventBus modBus) {
        CrittersAndCompanions.init();
        ForgeNetwork.register(modBus);
        ForgeConfigs.register(container);

        modBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(CrittersAndCompanions::setup));
        modBus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(CrittersAndCompanionsClient::init));

        var lootModifiers = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);
        lootModifiers.register("replace_item", () -> ReplaceItemModifier.CODEC);
        lootModifiers.register("add_item", () -> AddItemModifier.CODEC);
        lootModifiers.register("oak_leaves_acorn", () -> OakLeavesAcornModifier.CODEC);
        lootModifiers.register(modBus);
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(MODID, "resourcepacks/friendlyart"),
                PackType.CLIENT_RESOURCES,
                Component.literal("Friendly Critter Art"),
                PackSource.BUILT_IN,
                false,
                Pack.Position.BOTTOM
        );
    }

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        CrittersAndCompanions.onAttributeCreation(event::put);
    }

    @EventBusSubscriber(modid = MODID)
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
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            PlayerHandler.onPlayerTick(event.getEntity());
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