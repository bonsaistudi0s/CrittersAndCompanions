package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.block.SilkCocoonBlock;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class CACWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, CACWorldGenProvider::bootstrapConfiguredFeatures)
            .add(Registries.PLACED_FEATURE, CACWorldGenProvider::bootstrapPlacedFeatures);

    public CACWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("crittersandcompanions"));
    }

    public static void bootstrapConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> context) {
        ResourceKey<ConfiguredFeature<?, ?>> silkCocoon = ResourceKey.create(Registries.CONFIGURED_FEATURE, CrittersAndCompanions.createId("silk_cocoon"));
        ResourceKey<ConfiguredFeature<?, ?>> hangingSilkCocoon = ResourceKey.create(Registries.CONFIGURED_FEATURE, CrittersAndCompanions.createId("hanging_silk_cocoon"));

        context.register(silkCocoon, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(CACBlocks.SILK_COCOON.get()))));
        context.register(hangingSilkCocoon, new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(CACBlocks.SILK_COCOON.get().defaultBlockState().setValue(SilkCocoonBlock.HANGING, true)))));
    }

    public static void bootstrapPlacedFeatures(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        
        ResourceKey<PlacedFeature> silkCocoon = ResourceKey.create(Registries.PLACED_FEATURE, CrittersAndCompanions.createId("silk_cocoon"));
        ResourceKey<PlacedFeature> silkCocoonLush = ResourceKey.create(Registries.PLACED_FEATURE, CrittersAndCompanions.createId("silk_cocoon_lush"));
        ResourceKey<PlacedFeature> hangingSilkCocoon = ResourceKey.create(Registries.PLACED_FEATURE, CrittersAndCompanions.createId("hanging_silk_cocoon"));
        ResourceKey<PlacedFeature> hangingSilkCocoonLush = ResourceKey.create(Registries.PLACED_FEATURE, CrittersAndCompanions.createId("hanging_silk_cocoon_lush"));

        ResourceKey<ConfiguredFeature<?, ?>> silkCocoonConfigured = ResourceKey.create(Registries.CONFIGURED_FEATURE, CrittersAndCompanions.createId("silk_cocoon"));
        ResourceKey<ConfiguredFeature<?, ?>> hangingSilkCocoonConfigured = ResourceKey.create(Registries.CONFIGURED_FEATURE, CrittersAndCompanions.createId("hanging_silk_cocoon"));

        List<PlacementModifier> silkCocoonPlacement = List.of(
                CountPlacement.of(4),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(32), VerticalAnchor.absolute(140)),
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.alwaysTrue(), BlockPredicate.matchesBlocks(Blocks.AIR), 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome()
        );

        List<PlacementModifier> silkCocoonLushPlacement = List.of(
                CountPlacement.of(8),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(32), VerticalAnchor.absolute(140)),
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.alwaysTrue(), BlockPredicate.matchesBlocks(Blocks.AIR), 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome()
        );

        List<PlacementModifier> hangingSilkCocoonPlacement = List.of(
                CountPlacement.of(4),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(32), VerticalAnchor.absolute(140)),
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.matchesBlocks(Blocks.AIR), 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome()
        );

        List<PlacementModifier> hangingSilkCocoonLushPlacement = List.of(
                CountPlacement.of(8),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(32), VerticalAnchor.absolute(140)),
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.matchesBlocks(Blocks.AIR), 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome()
        );

        context.register(silkCocoon, new PlacedFeature(configuredFeatures.getOrThrow(silkCocoonConfigured), silkCocoonPlacement));
        context.register(silkCocoonLush, new PlacedFeature(configuredFeatures.getOrThrow(silkCocoonConfigured), silkCocoonLushPlacement));
        context.register(hangingSilkCocoon, new PlacedFeature(configuredFeatures.getOrThrow(hangingSilkCocoonConfigured), hangingSilkCocoonPlacement));
        context.register(hangingSilkCocoonLush, new PlacedFeature(configuredFeatures.getOrThrow(hangingSilkCocoonConfigured), hangingSilkCocoonLushPlacement));
    }
}
