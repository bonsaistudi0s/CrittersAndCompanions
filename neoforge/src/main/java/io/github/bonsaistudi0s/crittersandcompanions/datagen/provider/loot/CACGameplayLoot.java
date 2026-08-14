package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider.loot;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public final class CACGameplayLoot implements LootTableSubProvider {

    public CACGameplayLoot(HolderLookup.Provider provider) {
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        ResourceKey<LootTable> commonKey = ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(CrittersAndCompanions.MODID, "gameplay/digging/common")
        );
        ResourceKey<LootTable> trashKey = ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(CrittersAndCompanions.MODID, "gameplay/digging/trash")
        );
        ResourceKey<LootTable> treasureKey = ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(CrittersAndCompanions.MODID, "gameplay/digging/treasure")
        );
        ResourceKey<LootTable> diggingKey = ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(CrittersAndCompanions.MODID, "gameplay/digging")
        );

        consumer.accept(
                commonKey, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(Items.RABBIT_HIDE))
                                .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE))
                                .add(LootItem.lootTableItem(Items.GOLDEN_APPLE))
                                .add(LootItem.lootTableItem(Items.NAME_TAG))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
        );

        consumer.accept(
                trashKey, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(Items.BONE))
                                .add(LootItem.lootTableItem(Items.ROTTEN_FLESH))
                                .add(LootItem.lootTableItem(Items.STRING))
                                .add(LootItem.lootTableItem(Items.SWEET_BERRIES))
                                .add(LootItem.lootTableItem(Items.GOLD_NUGGET))
                                .add(LootItem.lootTableItem(Items.FLINT))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
        );

        consumer.accept(
                treasureKey, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(Items.TOTEM_OF_UNDYING)))
        );

        consumer.accept(
                diggingKey, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(NestedLootTable.lootTableReference(trashKey).setWeight(80))
                                .add(NestedLootTable.lootTableReference(commonKey).setWeight(18))
                                .add(NestedLootTable.lootTableReference(treasureKey).setWeight(2)))
        );
    }
}
