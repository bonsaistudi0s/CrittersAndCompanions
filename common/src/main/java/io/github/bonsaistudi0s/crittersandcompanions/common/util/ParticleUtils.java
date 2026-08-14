package io.github.bonsaistudi0s.crittersandcompanions.common.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class ParticleUtils {

    public static void drawParticleLine(ParticleOptions particleType, Level level, Vec3 pos1, Vec3 pos2, double space, Vec3 speed) {
        var direction = pos2.subtract(pos1).normalize();
        var distance = pos1.distanceTo(pos2);

        if (distance > 128) {
            return;
        }

        var lastPosition = pos1;
        for (double i = 0; i < distance; i += space) {
            var position = lastPosition.add(direction.scale(space));
            level.addParticle(particleType, position.x, position.y, position.z, speed.x, speed.y, speed.z);
            lastPosition = position;
        }
    }

    public static void drawParticlesWithRandomSpeed(ParticleOptions particleType, Level level, Vec3 position, int amount, double xOffset, double yOffset, double zOffset, double maxSpeed) {
        var random = level.random;
        for (var i = 0; i < amount; i++) {
            var x = position.x + xOffset * random.nextGaussian();
            var y = position.y + yOffset * random.nextGaussian();
            var z = position.z + zOffset * random.nextGaussian();
            var speedX = maxSpeed * random.nextGaussian();
            var speedY = maxSpeed * random.nextGaussian();
            var speedZ = maxSpeed * random.nextGaussian();
            level.addParticle(particleType, x, y, z, speedX, speedY, speedZ);
        }
    }

    public static void drawParticles(ParticleOptions particleType, Level level, Vec3 position, int amount, double xOffset, double yOffset, double zOffset, Vec3 speed) {
        var random = level.random;
        for (var i = 0; i < amount; i++) {
            var x = position.x + xOffset * random.nextGaussian();
            var y = position.y + yOffset * random.nextGaussian();
            var z = position.z + zOffset * random.nextGaussian();
            level.addParticle(particleType, x, y, z, speed.x, speed.y, speed.z);
        }
    }

    public static void drawPotionEffectLikeParticles(ParticleOptions particleType, Level level, AABB boundingBox, Vec3 speed, int amount) {
        for (var i = 0; i < amount; i++) {
            level.addParticle(particleType, BoundingBoxUtils.getRandomX(boundingBox, 0.5), BoundingBoxUtils.getRandomY(boundingBox), BoundingBoxUtils.getRandomZ(boundingBox, 0.5), speed.x, speed.y, speed.z);
        }
    }
}
