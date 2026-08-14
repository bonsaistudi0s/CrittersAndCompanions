package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider;

import io.github.bonsaistudi0s.crittersandcompanions.datagen.provider.loot.CACBlockLoot;
import io.github.bonsaistudi0s.crittersandcompanions.datagen.provider.loot.CACEntityLoot;
import io.github.bonsaistudi0s.crittersandcompanions.datagen.provider.loot.CACGameplayLoot;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class CACLootTableProvider {

    public static LootTableProvider create(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new LootTableProvider(
                packOutput,
                Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(CACBlockLoot::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(CACEntityLoot::new, LootContextParamSets.ENTITY),
                        new LootTableProvider.SubProviderEntry(CACGameplayLoot::new, LootContextParamSets.EMPTY)
                ),
                lookupProvider
        );
    }
}
