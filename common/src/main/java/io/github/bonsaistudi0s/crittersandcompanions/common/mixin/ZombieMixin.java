package io.github.bonsaistudi0s.crittersandcompanions.common.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.ServerLevelAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;

@Mixin(Zombie.class)
public class ZombieMixin {

    @Inject(
            method = "finalizeSpawn",
            at = @At("RETURN")
    )
    private void cac$injectJockeyVehicles(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType spawnType,
            SpawnGroupData spawnGroupData,
            CallbackInfoReturnable<SpawnGroupData> cir) {

        var finalGroupData = cir.getReturnValue();
        var canSpawnJockey = finalGroupData instanceof Zombie.ZombieGroupData zombieData && zombieData.isBaby && zombieData.canSpawnJockey;
        if (!canSpawnJockey) {
            return;
        }

        var self = (Zombie) (Object) this;

        // Zombie subclasses like ZombifiedPiglin inherit Zombie#finalizeSpawn
        if (self.getType() != EntityType.ZOMBIE) {
            return;
        }

        var didSpawnAsJockeyAlready = self.isPassenger();
        if (didSpawnAsJockeyAlready) {
            return;
        }

        // never run this in a world gen thread (if for some reason baby zombies get spawned in a structure)
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (serverLevel.getRandom().nextFloat() < 0.05F) {
            if (serverLevel.getRandom().nextBoolean()) {
                var mount = CACEntities.SNAIL.get().create(self.level());
                if (mount != null) {
                    mount.moveTo(self.getX(), self.getY(), self.getZ(), self.getYRot(), 0.0F);
                    mount.finalizeSpawn(serverLevel, difficulty, MobSpawnType.JOCKEY, null);
                    self.startRiding(mount);
                    serverLevel.addFreshEntity(mount);
                }
            } else {
                var mount = CACEntities.ROLY_POLY.get().create(self.level());
                if (mount != null) {
                    mount.moveTo(self.getX(), self.getY(), self.getZ(), self.getYRot(), 0.0F);
                    mount.finalizeSpawn(serverLevel, difficulty, MobSpawnType.JOCKEY, null);
                    self.startRiding(mount);
                    serverLevel.addFreshEntity(mount);
                }
            }
        }
    }
}
