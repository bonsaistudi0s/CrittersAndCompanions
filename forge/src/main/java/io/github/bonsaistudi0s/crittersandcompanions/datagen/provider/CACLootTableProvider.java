package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACBlocks;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public final class CACLootTableProvider {

    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(
                output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(CACBlockLoot::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(CACEntityLoot::new, LootContextParamSets.ENTITY),
                new LootTableProvider.SubProviderEntry(CACGameplayLoot::new, LootContextParamSets.ALL_PARAMS)
        )
        );
    }

    public static class CACBlockLoot extends BlockLootSubProvider {
        protected CACBlockLoot() {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            this.dropSelf(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get());

            LootItemCondition.Builder matchShears = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));

            this.add(
                    CACBlocks.SILK_COCOON.get(), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .when(matchShears)
                                    .add(LootItem.lootTableItem(CACBlocks.SILK_COCOON.get())))
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .when(matchShears.invert())
                                    .add(LootItem.lootTableItem(CACItems.SILK.get())
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(
                                                    3.0F,
                                                    5.0F
                                            )))))
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .when(matchShears.invert())
                                    .add(LootItem.lootTableItem(Items.ENDER_PEARL).when(LootItemRandomChanceCondition.randomChance(
                                            0.1F)))
                                    .add(LootItem.lootTableItem(Items.NAME_TAG).when(LootItemRandomChanceCondition.randomChance(
                                            0.1F))))
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2.0F))
                                    .when(matchShears.invert())
                                    .add(LootItem.lootTableItem(CACItems.DRAGONFLY_WING.get()).setWeight(1))
                                    .add(LootItem.lootTableItem(Items.SLIME_BALL).setWeight(1).apply(
                                            SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                    .add(LootItem.lootTableItem(Items.RABBIT_HIDE).setWeight(10).apply(
                                            SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                    .add(LootItem.lootTableItem(Items.SPIDER_EYE).setWeight(10).apply(
                                            SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                    .add(LootItem.lootTableItem(Items.COAL).setWeight(10).apply(SetItemCountFunction.setCount(
                                            UniformGenerator.between(1.0F, 3.0F))))
                                    .add(LootItem.lootTableItem(Items.IRON_NUGGET).setWeight(10).apply(
                                            SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                    .add(LootItem.lootTableItem(Items.BONE).setWeight(7).apply(SetItemCountFunction.setCount(
                                            UniformGenerator.between(1.0F, 3.0F)))))
            );
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return List.of(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get(), CACBlocks.SILK_COCOON.get());
        }
    }

    public static class CACEntityLoot extends EntityLootSubProvider {
        protected CACEntityLoot() {
            super(FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        public void generate() {
            this.add(
                    CACEntities.DRAGONFLY.get(), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(CACItems.DRAGONFLY_WING.get())
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(
                                                    0.0F,
                                                    2.0F
                                            )))))
            );

            this.add(
                    CACEntities.JUMPING_SPIDER.get(), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(CACItems.SILK.get())
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))))
                                    .add(LootItem.lootTableItem(Items.SPIDER_EYE)
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))))
                            )
            );

            this.add(
                    CACEntities.KOI_FISH.get(), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(CACItems.KOI_FISH.get())))
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .when(LootItemRandomChanceCondition.randomChance(0.05F))
                                    .add(LootItem.lootTableItem(Items.BONE_MEAL)))
            );

            this.add(
                    CACEntities.SHIMA_ENAGA.get(), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(Items.FEATHER)
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(
                                                    0.0F,
                                                    2.0F
                                            )))))
            );
        }

        @Override
        protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
            return Stream.of(
                    CACEntities.DRAGONFLY.get(),
                    CACEntities.JUMPING_SPIDER.get(),
                    CACEntities.KOI_FISH.get(),
                    CACEntities.SHIMA_ENAGA.get()
            );
        }
    }

    public static class CACGameplayLoot implements LootTableSubProvider {
        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            consumer.accept(
                    CrittersAndCompanions.createId("gameplay/digging"), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootTableReference.lootTableReference(CrittersAndCompanions.createId(
                                            "gameplay/digging/trash")).setWeight(80))
                                    .add(LootTableReference.lootTableReference(CrittersAndCompanions.createId(
                                            "gameplay/digging/common")).setWeight(18))
                                    .add(LootTableReference.lootTableReference(CrittersAndCompanions.createId(
                                            "gameplay/digging/treasure")).setWeight(2)))
            );

            consumer.accept(
                    CrittersAndCompanions.createId("gameplay/digging/common"), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(Items.RABBIT_HIDE))
                                    .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE))
                                    .add(LootItem.lootTableItem(Items.GOLDEN_APPLE))
                                    .add(LootItem.lootTableItem(Items.NAME_TAG))
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
            );

            consumer.accept(
                    CrittersAndCompanions.createId("gameplay/digging/trash"), LootTable.lootTable()
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
                    CrittersAndCompanions.createId("gameplay/digging/treasure"), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                    .add(LootItem.lootTableItem(Items.TOTEM_OF_UNDYING)))
            );
        }
    }
}
