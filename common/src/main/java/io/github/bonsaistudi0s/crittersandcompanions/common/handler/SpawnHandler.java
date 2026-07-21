package io.github.bonsaistudi0s.crittersandcompanions.common.handler;

import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.DragonflyEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.LeafInsectEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.OtterEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.base.AgeableWaterAnimal;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;

public class SpawnHandler {

    public static void registerSpawnPlacements() {
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
        return level.getBlockState(pos.below()).is(CACTags.BUG_SPAWN_GROUND) && level.getRawBrightness(pos, 0) >= 5;
    }
}
