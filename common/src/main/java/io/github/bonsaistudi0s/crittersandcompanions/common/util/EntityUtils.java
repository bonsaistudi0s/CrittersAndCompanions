package io.github.bonsaistudi0s.crittersandcompanions.common.util;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public abstract class EntityUtils {

    public static void applyAwarenessMaluses(PathfinderMob entity) {
        entity.setPathfindingMalus(BlockPathTypes.LAVA, -1.0F);
        entity.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        entity.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        entity.setPathfindingMalus(BlockPathTypes.DAMAGE_CAUTIOUS, 16.0F);
        entity.setPathfindingMalus(BlockPathTypes.DANGER_POWDER_SNOW, 8.0F);
    }
}
