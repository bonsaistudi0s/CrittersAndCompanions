/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.github.eterdelta.crittersandcompanions.datagen;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.*;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import net.minecraft.core.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


public class BiomeModifierProvider extends DatapackBuiltinEntriesProvider {


    private static final ResourceKey<BiomeModifier> ADD_RIVER_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CrittersAndCompanions.MODID, "add_river_spawns"));
    private static final ResourceKey<BiomeModifier> ADD_OCEANS_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CrittersAndCompanions.MODID, "add_oceans_spawns"));
    private static final ResourceKey<BiomeModifier> ADD_LUKEWARM_OCEANS_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CrittersAndCompanions.MODID, "add_lukewarm_oceans_spawns"));
    private static final ResourceKey<BiomeModifier> ADD_WARM_OCEANS_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CrittersAndCompanions.MODID, "add_warm_oceans_spawns"));
    private static final ResourceKey<BiomeModifier> ADD_FORESTS_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CrittersAndCompanions.MODID, "add_forests_spawns"));
    private static final ResourceKey<BiomeModifier> ADD_JUNGLES_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CrittersAndCompanions.MODID, "add_jungles_spawns"));
    private static final ResourceKey<BiomeModifier> ADD_PLAINS_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CrittersAndCompanions.MODID, "add_plains_spawns"));
    private static final ResourceKey<BiomeModifier> ADD_SNOWY_PLAINS_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CrittersAndCompanions.MODID, "add_snowy_plains_spawns"));
    private static final ResourceKey<BiomeModifier> ADD_LUSH_CAVES_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CrittersAndCompanions.MODID, "add_lush_caves_spawns"));


    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, context -> {
                context.lookup(ForgeRegistries.Keys.BIOMES);

                final HolderGetter<Biome> registry = context.lookup(ForgeRegistries.Keys.BIOMES);

                context.register(ADD_RIVER_SPAWNS, createSpawnModifier(
                        getBiomeHolderSet(registry, Biomes.RIVER),
                        new MobSpawnSettings.SpawnerData(CACEntities.OTTER.get(), 2, 3, 5),
                        new MobSpawnSettings.SpawnerData(CACEntities.KOI_FISH.get(), 4, 2, 5),
                        new MobSpawnSettings.SpawnerData(CACEntities.DRAGONFLY.get(), 6, 1, 1)
                ));

                context.register(ADD_OCEANS_SPAWNS, createSpawnModifier(
                        getBiomeHolderSet(registry, Biomes.OCEAN, Biomes.DEEP_OCEAN),
                        new MobSpawnSettings.SpawnerData(CACEntities.SEA_BUNNY.get(), 32, 1, 2),
                        new MobSpawnSettings.SpawnerData(CACEntities.DUMBO_OCTOPUS.get(), 4, 1, 1)
                                                                      ));

                context.register(ADD_LUKEWARM_OCEANS_SPAWNS, createSpawnModifier(
                        getBiomeHolderSet(registry, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN),
                        new MobSpawnSettings.SpawnerData(CACEntities.SEA_BUNNY.get(), 32, 1, 3),
                        new MobSpawnSettings.SpawnerData(CACEntities.DUMBO_OCTOPUS.get(), 6, 1, 1)
                ));

                context.register(ADD_WARM_OCEANS_SPAWNS, createSpawnModifier(
                        getBiomeHolderSet(registry, Biomes.WARM_OCEAN),
                        new MobSpawnSettings.SpawnerData(CACEntities.SEA_BUNNY.get(), 64, 1, 4),
                        new MobSpawnSettings.SpawnerData(CACEntities.DUMBO_OCTOPUS.get(), 6, 1, 1)
                                                                            ));

                context.register(ADD_FORESTS_SPAWNS, createSpawnModifier(
                        context.lookup(ForgeRegistries.Keys.BIOMES).getOrThrow(BiomeTags.IS_FOREST),
                        new MobSpawnSettings.SpawnerData(CACEntities.LEAF_INSECT.get(), 12, 1, 1),
                        new MobSpawnSettings.SpawnerData(CACEntities.FERRET.get(), 3, 2, 3),
                        new MobSpawnSettings.SpawnerData(CACEntities.JUMPING_SPIDER.get(), 2, 1, 1)
                ));

                context.register(ADD_JUNGLES_SPAWNS, createSpawnModifier(
                        context.lookup(ForgeRegistries.Keys.BIOMES).getOrThrow(BiomeTags.IS_JUNGLE),
                        new MobSpawnSettings.SpawnerData(CACEntities.LEAF_INSECT.get(), 12, 1, 1),
                        new MobSpawnSettings.SpawnerData(CACEntities.RED_PANDA.get(), 8, 1, 2),
                        new MobSpawnSettings.SpawnerData(CACEntities.JUMPING_SPIDER.get(), 2, 1, 1)
                ));

                context.register(ADD_PLAINS_SPAWNS, createSpawnModifier(
                        getBiomeHolderSet(registry, Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS),
                        new MobSpawnSettings.SpawnerData(CACEntities.FERRET.get(), 4, 2, 3)
                ));

                context.register(ADD_SNOWY_PLAINS_SPAWNS, createSpawnModifier(
                        getBiomeHolderSet(registry, Biomes.SNOWY_PLAINS),
                        new MobSpawnSettings.SpawnerData(CACEntities.SHIMA_ENAGA.get(), 3, 2, 3)
                ));

                context.register(ADD_LUSH_CAVES_SPAWNS, createSpawnModifier(
                        getBiomeHolderSet(registry, Biomes.LUSH_CAVES),
                        new MobSpawnSettings.SpawnerData(CACEntities.JUMPING_SPIDER.get(), 2, 1, 1)
                ));


            });

    @SafeVarargs
    public static HolderSet<Biome> getBiomeHolderSet(HolderGetter<Biome> context, ResourceKey<Biome>... biomes) {
        return HolderSet.direct(Arrays.stream(biomes).map(context::getOrThrow).toList());
    }

    public static BiomeModifier createSpawnModifier(HolderSet<Biome> biomes, MobSpawnSettings.SpawnerData... spawnerData) {
        return (spawnerData.length == 1)
                ? ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(biomes, spawnerData[0])
                : new ForgeBiomeModifiers.AddSpawnsBiomeModifier(biomes, Arrays.stream(spawnerData).toList());
    }

    public BiomeModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(CrittersAndCompanions.MODID));
    }

    public static void registerSpawnPlacements() {
        SpawnPlacements.register(CACEntities.OTTER.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OtterEntity::checkOtterSpawnRules);
        SpawnPlacements.register(CACEntities.KOI_FISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(CACEntities.DRAGONFLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, DragonflyEntity::checkDragonflySpawnRules);
        SpawnPlacements.register(CACEntities.SEA_BUNNY.get(), SpawnPlacements.Type.create("ON_WATER_GROUND",
                                                                                          ((levelReader, blockPos, entityType) -> levelReader.getFluidState(blockPos).is(FluidTags.WATER) && levelReader.getBlockState(blockPos.below()).isFaceSturdy(levelReader, blockPos.below(), Direction.UP))), Heightmap.Types.OCEAN_FLOOR, SeaBunnyEntity::checkSeaBunnySpawnRules);
        SpawnPlacements.register(CACEntities.FERRET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacements.register(CACEntities.DUMBO_OCTOPUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DumboOctopusEntity::checkDumboOctopusSpawnRules);
        SpawnPlacements.register(CACEntities.LEAF_INSECT.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, LeafInsectEntity::checkLeafInsectSpawnRules);
        SpawnPlacements.register(CACEntities.RED_PANDA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    }

    @Override
    public String getName() {
        return "CAC Biome Modifiers";
    }

}