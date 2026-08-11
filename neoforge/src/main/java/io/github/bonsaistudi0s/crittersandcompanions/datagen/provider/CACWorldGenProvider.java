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
import net.minecraft.data.worldgen.BootstrapContext;
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
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class CACWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final ResourceKey<ConfiguredFeature<?, ?>> SILK_COCOON_CF = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            CrittersAndCompanions.createId("silk_cocoon")
    );
    public static final ResourceKey<ConfiguredFeature<?, ?>> HANGING_SILK_COCOON_CF = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            CrittersAndCompanions.createId("hanging_silk_cocoon")
    );

    public static final ResourceKey<PlacedFeature> SILK_COCOON_PF = ResourceKey.create(
            Registries.PLACED_FEATURE,
            CrittersAndCompanions.createId("silk_cocoon")
    );
    public static final ResourceKey<PlacedFeature> SILK_COCOON_LUSH_PF = ResourceKey.create(
            Registries.PLACED_FEATURE,
            CrittersAndCompanions.createId("silk_cocoon_lush")
    );
    public static final ResourceKey<PlacedFeature> HANGING_SILK_COCOON_PF = ResourceKey.create(
            Registries.PLACED_FEATURE,
            CrittersAndCompanions.createId("hanging_silk_cocoon")
    );
    public static final ResourceKey<PlacedFeature> HANGING_SILK_COCOON_LUSH_PF = ResourceKey.create(
            Registries.PLACED_FEATURE,
            CrittersAndCompanions.createId("hanging_silk_cocoon_lush")
    );

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, CACWorldGenProvider::bootstrapConfiguredFeatures)
            .add(Registries.PLACED_FEATURE, CACWorldGenProvider::bootstrapPlacedFeatures);

    public CACWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(CrittersAndCompanions.MODID));
    }

    private static void bootstrapConfiguredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(
                SILK_COCOON_CF,
                new ConfiguredFeature<>(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(CACBlocks.SILK_COCOON.get()))
                )
        );
        context.register(
                HANGING_SILK_COCOON_CF,
                new ConfiguredFeature<>(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(CACBlocks.SILK_COCOON.get().defaultBlockState().setValue(
                                SilkCocoonBlock.HANGING,
                                true
                        )))
                )
        );
    }

    private static void bootstrapPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(
                SILK_COCOON_PF, new PlacedFeature(
                        configuredFeatures.getOrThrow(SILK_COCOON_CF), List.of(
                        CountPlacement.of(4),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(60), VerticalAnchor.absolute(140)),
                        EnvironmentScanPlacement.scanningFor(
                                Direction.DOWN,
                                BlockPredicate.alwaysTrue(),
                                BlockPredicate.matchesBlocks(Blocks.AIR),
                                12
                        ),
                        RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                        BlockPredicateFilter.forPredicate(BlockPredicate.not(BlockPredicate.matchesBlocks(Blocks.WATER))),
                        BiomeFilter.biome()
                )
                )
        );

        context.register(
                SILK_COCOON_LUSH_PF, new PlacedFeature(
                        configuredFeatures.getOrThrow(SILK_COCOON_CF), List.of(
                        CountPlacement.of(8),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(32), VerticalAnchor.absolute(140)),
                        EnvironmentScanPlacement.scanningFor(
                                Direction.DOWN,
                                BlockPredicate.alwaysTrue(),
                                BlockPredicate.matchesBlocks(Blocks.AIR),
                                12
                        ),
                        RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                        BlockPredicateFilter.forPredicate(BlockPredicate.not(BlockPredicate.matchesBlocks(Blocks.WATER))),
                        BiomeFilter.biome()
                )
                )
        );

        context.register(
                HANGING_SILK_COCOON_PF, new PlacedFeature(
                        configuredFeatures.getOrThrow(HANGING_SILK_COCOON_CF), List.of(
                        CountPlacement.of(4),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(60), VerticalAnchor.absolute(140)),
                        EnvironmentScanPlacement.scanningFor(
                                Direction.UP,
                                BlockPredicate.hasSturdyFace(Direction.DOWN),
                                BlockPredicate.matchesBlocks(Blocks.AIR),
                                12
                        ),
                        RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                        BlockPredicateFilter.forPredicate(BlockPredicate.not(BlockPredicate.matchesBlocks(Blocks.WATER))),
                        BiomeFilter.biome()
                )
                )
        );

        context.register(
                HANGING_SILK_COCOON_LUSH_PF, new PlacedFeature(
                        configuredFeatures.getOrThrow(HANGING_SILK_COCOON_CF), List.of(
                        CountPlacement.of(8),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(32), VerticalAnchor.absolute(140)),
                        EnvironmentScanPlacement.scanningFor(
                                Direction.UP,
                                BlockPredicate.hasSturdyFace(Direction.DOWN),
                                BlockPredicate.matchesBlocks(Blocks.AIR),
                                12
                        ),
                        RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                        BlockPredicateFilter.forPredicate(BlockPredicate.not(BlockPredicate.matchesBlocks(Blocks.WATER))),
                        BiomeFilter.biome()
                )
                )
        );
    }
}
