package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider.loot;

import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACBlocks;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class CACBlockLoot extends BlockLootSubProvider {

    public CACBlockLoot(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        this.add(
                CACBlocks.SEA_BUNNY_SLIME_BLOCK.get(), LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .when(ExplosionCondition.survivesExplosion())
                                .add(LootItem.lootTableItem(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get())))
        );

        var shearsCondition = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
        var noShears = InvertedLootItemCondition.invert(shearsCondition);

        this.add(
                CACBlocks.SILK_COCOON.get(), LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .when(shearsCondition)
                                .add(LootItem.lootTableItem(CACBlocks.SILK_COCOON.get())))
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .when(noShears)
                                .add(LootItem.lootTableItem(CACItems.SILK.get())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F)))))
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .when(noShears)
                                .add(LootItem.lootTableItem(Items.ENDER_PEARL)
                                        .when(LootItemRandomChanceCondition.randomChance(0.1F)))
                                .add(LootItem.lootTableItem(Items.NAME_TAG)
                                        .when(LootItemRandomChanceCondition.randomChance(0.1F))))
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2.0F))
                                .when(noShears)
                                .add(LootItem.lootTableItem(CACItems.DRAGONFLY_WING.get()))
                                .add(LootItem.lootTableItem(Items.SLIME_BALL)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(Items.RABBIT_HIDE).setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.COAL).setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.BONE).setWeight(7)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        )
        );
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        java.util.List<Block> blocks = new java.util.ArrayList<>();
        CACBlocks.BLOCKS.iterator().forEachRemaining(supplier -> blocks.add(supplier.get()));
        return blocks;
    }
}
