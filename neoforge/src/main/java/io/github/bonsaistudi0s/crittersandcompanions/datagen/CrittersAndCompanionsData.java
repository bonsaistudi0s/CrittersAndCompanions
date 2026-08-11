package io.github.bonsaistudi0s.crittersandcompanions.datagen;

import io.github.bonsaistudi0s.crittersandcompanions.datagen.provider.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber()
public class CrittersAndCompanionsData {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var output = event.getGenerator().getPackOutput();
        var fileHelper = event.getExistingFileHelper();
        var lookup = event.getLookupProvider();

        event.addProvider(new CACLang(output));
        event.addProvider(new CACItemModels(output, fileHelper));
        event.addProvider(new CACRecipes(output, lookup));
        event.addProvider(CACLootTableProvider.create(output, lookup));

        var blockTags = event.addProvider(new CACBlockTags(output, lookup, fileHelper));
        event.addProvider(new CACEntityTags(output, lookup, fileHelper));
        event.addProvider(new CACBiomeTags(output, lookup, fileHelper));
        event.addProvider(new CACItemTags(output, lookup, blockTags.contentsGetter(), fileHelper));
        
        event.addProvider(new CACWorldGenProvider(output, lookup));
    }

}
