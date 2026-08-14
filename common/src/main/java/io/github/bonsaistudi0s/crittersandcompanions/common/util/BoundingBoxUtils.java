package io.github.bonsaistudi0s.crittersandcompanions.common.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;

public abstract class BoundingBoxUtils {

    private static final RandomSource random = RandomSource.create();

    private static double getX(AABB boundingBox) {
        return boundingBox.minX + boundingBox.getXsize() / 2.0;
    }

    private static double getX(AABB boundingBox, double scale) {
        return getX(boundingBox) + boundingBox.getXsize() * scale;
    }

    public static double getRandomX(AABB boundingBox, double scale) {
        return getX(boundingBox, (2.0 * random.nextDouble() - 1.0) * scale);
    }

    private static double getY(AABB boundingBox, double scale) {
        return boundingBox.minY + boundingBox.getYsize() * scale;
    }

    public static double getRandomY(AABB boundingBox) {
        return getY(boundingBox, random.nextDouble());
    }

    private static double getZ(AABB boundingBox) {
        return boundingBox.minZ + boundingBox.getZsize() / 2.0;
    }

    private static double getZ(AABB boundingBox, double scale) {
        return getZ(boundingBox) + boundingBox.getZsize() * scale;
    }

    public static double getRandomZ(AABB boundingBox, double scale) {
        return getZ(boundingBox, (2.0 * random.nextDouble() - 1.0) * scale);
    }
}
