package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.loot.v2.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.mixin.loot.LootTableAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class CACLootModifiers {

    public static void register() {
        // should always reflect the values in crittersandcompanions/loot_modifiers/..

        addEntriesTo(BuiltInLootTables.FISHING_FISH, 0, builder -> {
            builder.accept(10, new ItemStack(CACItems.CLAM.get()));
            builder.accept(5, new ItemStack(CACItems.KOI_FISH.get()));
        });

        addEntriesTo(EntityType.DROWNED.getDefaultLootTable(), 0, builder -> {
            builder.accept(10, new ItemStack(CACItems.CLAM.get()));
        });

        LootTableEvents.MODIFY.register((resources, manager, key, builder, source) -> {
            if (
                    key.equals(BuiltInLootTables.SHIPWRECK_TREASURE)
                            || key.equals(BuiltInLootTables.UNDERWATER_RUIN_SMALL)
                            || key.equals(BuiltInLootTables.UNDERWATER_RUIN_BIG)
            ) builder.withPool(
                    LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1F))
                            .when(LootItemRandomChanceCondition.randomChance(0.1F))
                            .add(LootItem.lootTableItem(CACItems.CLAM.get()))
            );
        });
    }

    private static void addEntriesTo(ResourceLocation key, int index, Consumer<BiConsumer<Integer, ItemStack>> entries) {
        LootTableEvents.REPLACE.register(((resources, lootManager, id, table, source) -> {
            if (!id.equals(key)) return null;

            var accessor = (LootTableAccessor) table;
            var builder = LootTable.lootTable()
                    .setRandomSequence(accessor.fabric_getRandomSequenceId())
                    .setParamSet(table.getParamSet());

            for (int i = 0; i < table.pools.length; i++) {
                var pool = FabricLootPoolBuilder.copyOf(table.pools[i]);

                if (i == index) {
                    entries.accept((weight, stack) -> {
                        pool.add(LootItem.lootTableItem(stack.getItem())
                                .setWeight(weight)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(stack.getCount())))
                        );
                    });
                }

                builder.withPool(pool);
            }

            return builder.build();
        }));
    }

}
