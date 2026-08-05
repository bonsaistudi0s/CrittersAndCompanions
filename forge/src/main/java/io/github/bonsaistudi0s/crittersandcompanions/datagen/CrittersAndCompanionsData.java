package io.github.bonsaistudi0s.crittersandcompanions.datagen;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.datagen.provider.*;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CrittersAndCompanionsData {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var fileHelper = event.getExistingFileHelper();
        var lookup = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new CACLang(output));
        generator.addProvider(event.includeClient(), new CACItemModels(output, fileHelper));

        var blockTags = generator.addProvider(event.includeServer(), new CACBlockTags(output, lookup, fileHelper));
        generator.addProvider(event.includeServer(), new CACEntityTags(output, lookup, fileHelper));
        generator.addProvider(event.includeServer(), new CACBiomeTags(output, lookup, fileHelper));
        generator.addProvider(event.includeServer(), new CACItemTags(output, lookup, blockTags.contentsGetter(), fileHelper));
        generator.addProvider(event.includeServer(), new CACDamageTypeTags(output, lookup, fileHelper));

        generator.addProvider(event.includeServer(), new CACRecipeProvider(output));
        generator.addProvider(event.includeServer(), CACLootTableProvider.create(output));
        generator.addProvider(event.includeServer(), new CACWorldGenProvider(output, lookup));
    }

}
