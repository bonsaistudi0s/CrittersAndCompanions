package com.github.eterdelta.crittersandcompanions.handler;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.mixin.LootPoolAccessor;
import com.github.eterdelta.crittersandcompanions.mixin.LootTableAccessor;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
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
        ResourceLocation tableName = event.getName();
        LootTable table = event.getTable();

        if (tableName.equals(BuiltInLootTables.FISHING_FISH)) {
            LootPool fishPool = ((LootTableAccessor) table).getPools().get(0);

            ((LootTableAccessor) table).getPools().remove(0);
            table.addPool(addPoolEntries(fishPool,
                    LootItem.lootTableItem(CACItems.CLAM.get()).setWeight(10).when(AlternativeLootItemCondition.alternative(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiome(Biomes.RIVER)))).build(),
                    LootItem.lootTableItem(CACItems.KOI_FISH.get()).setWeight(5).build()));
        } else if (tableName.equals(EntityType.DROWNED.getDefaultLootTable())) {
            LootPool deathPool = ((LootTableAccessor) table).getPools().get(0);

            ((LootTableAccessor) table).getPools().remove(0);
            table.addPool(addPoolEntries(deathPool,
                    LootItem.lootTableItem(CACItems.CLAM.get()).setWeight(10).build()));
        }
    }

    private static LootPool addPoolEntries(LootPool mainPool, LootPoolEntryContainer... entries) {
        LootPoolAccessor mainPoolAccessor = ((LootPoolAccessor) mainPool);
        LootPoolEntryContainer[] addedEntries = ArrayUtils.addAll(mainPoolAccessor.getEntries(), entries);

        return LootPoolAccessor.make(addedEntries, mainPoolAccessor.getConditions(), mainPoolAccessor.getFunctions(), mainPool.getRolls(), mainPool.getBonusRolls(), mainPool.getName());
    }
}
