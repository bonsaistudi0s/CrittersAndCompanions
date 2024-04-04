package earth.terrarium.crittersandcompanions.fabric;


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
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class CrittersAndCompanionsFabric {

    public static void init() {
        CrittersAndCompanions.init();

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
