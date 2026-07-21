package io.github.bonsaistudi0s.crittersandcompanions.fabric;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.handler.PlayerHandler;
import io.github.bonsaistudi0s.crittersandcompanions.fabric.common.loot.CACLootModifiers;
import io.github.bonsaistudi0s.crittersandcompanions.fabric.common.world.CACWorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;

public final class CrittersAndCompanionsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CrittersAndCompanions.init();

        EntityTrackingEvents.START_TRACKING.register(PlayerHandler::onPlayerStartTracking);
        EntityTrackingEvents.STOP_TRACKING.register(PlayerHandler::onPlayerStopTracking);

        CACWorldGen.register();
        CACLootModifiers.register();
    }
}
