package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.handler.PlayerHandler;
import com.github.eterdelta.crittersandcompanions.platform.FabricConfigs;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityInteractCallback;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerTickEvents;
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

        EntityInteractCallback.EVENT.register((player, hand, target) -> {
            if (player.level().isClientSide()) return null;
            return PlayerHandler.onPlayerEntityInteract(target, new UseOnContext(player, hand, null));
        });

        PlayerTickEvents.END.register(PlayerHandler::onPlayerTick);
        EntityTrackingEvents.START_TRACKING.register(PlayerHandler::onPlayerStartTracking);
        EntityTrackingEvents.STOP_TRACKING.register(PlayerHandler::onPlayerStopTracking);

        FabricConfigs.register();
        CACWorldGen.register();
        CACLootModifiers.register();
    }

}
