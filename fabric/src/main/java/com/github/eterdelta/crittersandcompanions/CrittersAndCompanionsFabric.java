package com.github.eterdelta.crittersandcompanions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class CrittersAndCompanionsFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        CrittersAndCompanions.init();
        CrittersAndCompanions.setup();
    }

    @Override
    public void onInitializeClient() {
        CrittersAndCompanions.clientSetup();
    }

}
