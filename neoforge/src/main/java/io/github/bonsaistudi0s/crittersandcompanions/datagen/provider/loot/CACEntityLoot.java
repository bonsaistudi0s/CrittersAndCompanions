package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider.loot;

import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public final class CACEntityLoot extends EntityLootSubProvider {

    public CACEntityLoot(HolderLookup.Provider provider) {
        super(FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    public void generate() {
        this.add(
                CACEntities.DRAGONFLY.get(), LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(CACItems.DRAGONFLY_WING.get())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))))
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
                                .add(LootItem.lootTableItem(CACItems.KOI_FISH.get())
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))))
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .when(LootItemRandomChanceCondition.randomChance(0.05F))
                                .add(LootItem.lootTableItem(Items.BONE_MEAL)))
        );

        this.add(
                CACEntities.SHIMA_ENAGA.get(), LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                                .add(LootItem.lootTableItem(Items.FEATHER)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))))
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
