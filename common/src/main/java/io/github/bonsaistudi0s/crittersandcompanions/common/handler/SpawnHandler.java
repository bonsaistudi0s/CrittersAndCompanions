package io.github.bonsaistudi0s.crittersandcompanions.common.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;

import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.DragonflyEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.LeafInsectEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.OtterEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACTags;

public class SpawnHandler {

    public static void registerSpawnPlacements() {
        SpawnPlacementsRegistry.register(CACEntities.OTTER, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OtterEntity::checkOtterSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.KOI_FISH, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.DRAGONFLY, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, DragonflyEntity::checkDragonflySpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.SEA_BUNNY, SpawnPlacementTypes.IN_WATER, Heightmap.Types.OCEAN_FLOOR, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.FERRET, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.DUMBO_OCTOPUS, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.LEAF_INSECT, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, LeafInsectEntity::checkLeafInsectSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.RED_PANDA, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.SHIMA_ENAGA, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Animal::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.JUMPING_SPIDER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (e, l, s, p, r) -> true);
        SpawnPlacementsRegistry.register(CACEntities.LADYBUG, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.STAG_BEETLE, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.ROLY_POLY, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.SNAIL, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.STICK_BUG, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.WEEVIL, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SpawnHandler::checkBugSpawnRules);
    }

    private static boolean checkBugSpawnRules(EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(CACTags.BUG_SPAWN_GROUND) && level.getRawBrightness(pos, 0) >= 5;
    }
}
