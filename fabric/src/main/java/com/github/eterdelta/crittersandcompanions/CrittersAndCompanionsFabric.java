package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.config.CACSpawnConfig;
import com.github.eterdelta.crittersandcompanions.handler.PlayerHandler;
import com.github.eterdelta.crittersandcompanions.platform.FabricConfigs;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerInteractEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.PlayerTickEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.item.context.UseOnContext;

public class CrittersAndCompanionsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CrittersAndCompanions.init();
        CrittersAndCompanions.setup();

        CrittersAndCompanions.onAttributeCreation(FabricDefaultAttributeRegistry::register);

        // TODO check
        PlayerInteractEvent.EntityInteract.EVENT.register(event -> {
            if (event.getLevel().isClientSide()) return;
            var result = PlayerHandler.onPlayerEntityInteract(event.getTarget(), new UseOnContext(event.getEntity(), event.getHand(), null));
            if (result != null) {
                event.setCancellationResult(result);
                event.setCanceled(true);
            }
        });

        PlayerTickEvent.Post.EVENT.register(event -> PlayerHandler.onPlayerTick(event.getEntity()));
        EntityTrackingEvents.START_TRACKING.register(PlayerHandler::onPlayerStartTracking);
        EntityTrackingEvents.STOP_TRACKING.register(PlayerHandler::onPlayerStopTracking);

        FabricConfigs.register();
        CACSpawnConfig.load(Services.PLATFORM.getConfigDir());
        CACWorldGen.register();
        CACLootModifiers.register();
    }
}
