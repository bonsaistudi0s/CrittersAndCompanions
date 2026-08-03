package io.github.bonsaistudi0s.crittersandcompanions.common.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACSpawnConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.DragonflyEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.LeafInsectEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.OtterEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.base.AgeableWaterAnimal;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACTags;

public class SpawnHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpawnHandler.class);

    public static void register() {
        registerSpawns();
        registerSpawnPlacements();
    }

    private static void registerSpawns() {
        var spawningConfig = CACCommonConfig.HANDLER.instance().spawning;

        for (var entityEntry : spawningConfig.getAllEntries().entrySet()) {
            var entityPath = entityEntry.getKey();
            var type = CACSpawnConfig.resolveEntityType(entityPath);
            if (type == null) {
                LOGGER.warn("Unknown entity '{}' in spawn config, skipping", entityPath);
                continue;
            }

            for (var spawn : entityEntry.getValue()) {
                if (spawn.weight() <= 0) {
                    continue;
                }

                var spawnerData = new MobSpawnSettings.SpawnerData(type, spawn.weight(), spawn.min(), spawn.max());

                if (spawn.isTag()) {
                    var tag = TagKey.create(Registries.BIOME, spawn.biomeLocation());
                    BiomeModifications.addProperties(context -> context.hasTag(tag), (context, properties) -> properties.getSpawnProperties().addSpawn(type.getCategory(), spawnerData));
                } else {
                    BiomeModifications.addProperties(context -> context.getKey().isPresent() && context.getKey().get().equals(spawn.biomeLocation()), (context, properties) -> properties.getSpawnProperties().addSpawn(type.getCategory(), spawnerData));
                }
            }
        }
    }

    private static void registerSpawnPlacements() {
        SpawnPlacementsRegistry.register(CACEntities.OTTER, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OtterEntity::checkOtterSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.KOI_FISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.DRAGONFLY, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, DragonflyEntity::checkDragonflySpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.SEA_BUNNY, SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, AgeableWaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.FERRET, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.DUMBO_OCTOPUS, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.LEAF_INSECT, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, LeafInsectEntity::checkLeafInsectSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.RED_PANDA, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.SHIMA_ENAGA, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Animal::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.JUMPING_SPIDER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (e, l, s, p, r) -> true);
        SpawnPlacementsRegistry.register(CACEntities.LADYBUG, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.STAG_BEETLE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.ROLY_POLY, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.SNAIL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.STICK_BUG, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.WEEVIL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
    }

    private static boolean checkBugSpawnRules(EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        var belowState = level.getBlockState(pos.below());
        var isValidFloor = belowState.is(CACTags.BUG_SPAWN_GROUND);

        var isLushCave = level.getBiome(pos).is(Biomes.LUSH_CAVES);
        if (isLushCave) {
            if (level.getBrightness(LightLayer.BLOCK, pos) == 0) {
                return false;
            }

            return isValidFloor || belowState.is(BlockTags.BASE_STONE_OVERWORLD);
        }

        return isValidFloor && level.getRawBrightness(pos, 0) >= 5;
    }
}
