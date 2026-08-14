package io.github.bonsaistudi0s.crittersandcompanions.common.util;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.pathfinder.PathType;

public abstract class EntityUtils {

    public static void applyAwarenessMaluses(PathfinderMob entity) {
        entity.setPathfindingMalus(PathType.LAVA, -1.0F);
        entity.setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
        entity.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        entity.setPathfindingMalus(PathType.DAMAGE_CAUTIOUS, 16.0F);
        entity.setPathfindingMalus(PathType.DANGER_TRAPDOOR, 8.0F);
        entity.setPathfindingMalus(PathType.DANGER_POWDER_SNOW, 8.0F);
    }
}
