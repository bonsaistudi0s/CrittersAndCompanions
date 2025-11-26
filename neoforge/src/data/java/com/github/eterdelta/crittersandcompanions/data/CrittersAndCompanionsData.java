package com.github.eterdelta.crittersandcompanions.data;

import com.github.eterdelta.crittersandcompanions.data.provider.CACItemModels;
import com.github.eterdelta.crittersandcompanions.data.provider.CACLang;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class CrittersAndCompanionsData {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var output = event.getGenerator().getPackOutput();
        var fileHelper = event.getExistingFileHelper();

        event.addProvider(new CACLang(output));
        event.addProvider(new CACItemModels(output, fileHelper));
    }

}
