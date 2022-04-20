package com.github.eterdelta.crittersandcompanions.handler;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.mixin.LootPoolAccessor;
import com.github.eterdelta.crittersandcompanions.registry.CaCItems;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.AlternativeLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.ArrayUtils;

@Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID)
public class LootHandler {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().equals(BuiltInLootTables.FISHING_FISH)) {
            LootTable fishTable = event.getTable();
            LootPool fishPool = fishTable.getPool("main");

            fishTable.removePool("main");
            fishTable.addPool(addPoolEntries(fishPool,
                    LootItem.lootTableItem(CaCItems.CLAM.get()).setWeight(10).when(AlternativeLootItemCondition.alternative(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.RIVER)))).build(),
                    LootItem.lootTableItem(CaCItems.KOI_FISH.get()).setWeight(5).build()));
        }
    }

    private static LootPool addPoolEntries(LootPool mainPool, LootPoolEntryContainer... entries) {
        LootPoolAccessor mainPoolAccessor = ((LootPoolAccessor) mainPool);
        LootPoolEntryContainer[] addedEntries = ArrayUtils.addAll(mainPoolAccessor.getEntries(), entries);

        return LootPoolAccessor.make(addedEntries, mainPoolAccessor.getConditions(), mainPoolAccessor.getFunctions(), mainPool.getRolls(), mainPool.getBonusRolls(), mainPool.getName());
    }
}
