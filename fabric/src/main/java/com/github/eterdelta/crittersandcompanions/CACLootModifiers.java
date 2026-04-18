package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.registry.CACItems;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.loot.v3.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.mixin.loot.LootTableAccessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class CACLootModifiers {

    private static final ResourceKey<LootTable> OAK_LEAVES =
            ResourceKey.create(Registries.LOOT_TABLE,
                    ResourceLocation.withDefaultNamespace("blocks/oak_leaves"));

    public static void register() {
        // should always reflect the values in crittersandcompanions/loot_modifiers/..

        addEntriesTo(BuiltInLootTables.FISHING_FISH, 0, builder -> {
            builder.accept(10, new ItemStack(CACItems.CLAM.get()));
            builder.accept(5, new ItemStack(CACItems.KOI_FISH.get()));
        });

        addEntriesTo(EntityType.DROWNED.getDefaultLootTable(), 0, builder -> {
            builder.accept(1, new ItemStack(CACItems.CLAM.get()));
        });

        LootTableEvents.MODIFY.register((key, builder, source, provider) -> {
            if (key.equals(BuiltInLootTables.SHIPWRECK_TREASURE)
                    || key.equals(BuiltInLootTables.UNDERWATER_RUIN_SMALL)
                    || key.equals(BuiltInLootTables.UNDERWATER_RUIN_BIG)
            ) {
                builder.withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1F))
                                .when(LootItemRandomChanceCondition.randomChance(0.1F))
                                .add(LootItem.lootTableItem(CACItems.CLAM.get()))
                );
            }

            if (key.equals(OAK_LEAVES)) {
                var silkTouchHolder = provider.lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(Enchantments.SILK_TOUCH);
                var fortuneHolder = provider.lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(Enchantments.FORTUNE);

                builder.withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1F))
                                .when(InvertedLootItemCondition.invert(
                                        AnyOfCondition.anyOf(
                                                MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS)),
                                                BonusLevelTableCondition.bonusLevelFlatChance(
                                                        silkTouchHolder,
                                                        0.0f,
                                                        1.0f
                                                )
                                        )
                                ))
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(
                                        fortuneHolder,
                                        0.005f,
                                        0.0055555557f,
                                        0.00625f,
                                        0.008333334f,
                                        0.025f
                                ))
                                .add(LootItem.lootTableItem(CACItems.ACORN.get()))
                );
            }
        });
    }

    private static void addEntriesTo(ResourceKey<LootTable> key, int index,
                                     Consumer<BiConsumer<Integer, ItemStack>> entries) {
        LootTableEvents.REPLACE.register(((id, table, source, lookupProvider) -> {
            if (!id.equals(key)) return null;

            var accessor = (LootTableAccessor) table;
            var builder = LootTable.lootTable()
                    .setParamSet(table.getParamSet());

            accessor.fabric_getRandomSequenceId().ifPresent(builder::setRandomSequence);
            var pools = accessor.fabric_getPools();

            for (int i = 0; i < pools.size(); i++) {
                var pool = FabricLootPoolBuilder.copyOf(pools.get(i));

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
