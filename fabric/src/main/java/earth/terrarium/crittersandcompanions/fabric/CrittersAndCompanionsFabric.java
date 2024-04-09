package earth.terrarium.crittersandcompanions.fabric;


import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.handler.AnimalHandler;
import earth.terrarium.crittersandcompanions.common.handler.PlayerHandler;
import earth.terrarium.crittersandcompanions.common.handler.SpawnHandler;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class CrittersAndCompanionsFabric implements ModInitializer {

    public void onInitialize() {
        CrittersAndCompanions.init();
        AnimalHandler.onAttributeCreation(FabricDefaultAttributeRegistry::register);
        AnimalHandler.registerSpawnPlacements();

        ItemGroupEvents.modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(CrittersAndCompanions.MODID, "main"))).register(group -> {
            ModItems.ITEMS.stream().map(Supplier::get).forEach(group::accept);
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            AtomicReference<InteractionResult> result = new AtomicReference<>(InteractionResult.PASS);
            PlayerHandler.onPlayerEntityInteract(player, hand, entity, aBoolean -> {}, result::set);
            return result.get();
        });

        EntityTrackingEvents.START_TRACKING.register((entity, player) -> {
            PlayerHandler.onPlayerStartTracking(player, entity);
        });

        EntityTrackingEvents.STOP_TRACKING.register((entity, player) -> {
            PlayerHandler.onPlayerStopTracking(player, entity);
        });
    }
}
