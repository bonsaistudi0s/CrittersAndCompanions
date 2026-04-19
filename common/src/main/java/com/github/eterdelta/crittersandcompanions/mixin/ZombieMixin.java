package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
        Zombie self = (Zombie) (Object) this;

        if (!self.isBaby() || self.isPassenger()) {
            return;
        }

        if (level.getRandom().nextFloat() < 0.1F) {
            if (level.getRandom().nextBoolean()) {
                var mount = CACEntities.SNAIL.get().create(self.level());
                if (mount != null) {
                    mount.moveTo(self.getX(), self.getY(), self.getZ(), self.getYRot(), 0.0F);
                    mount.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null);
                    self.startRiding(mount);
                    level.addFreshEntity(mount);
                }
            } else {
                var mount = CACEntities.ROLLYPOLLY.get().create(self.level());
                if (mount != null) {
                    mount.moveTo(self.getX(), self.getY(), self.getZ(), self.getYRot(), 0.0F);
                    mount.finalizeSpawn(level, difficulty, MobSpawnType.JOCKEY, null);
                    self.startRiding(mount);
                    level.addFreshEntity(mount);
                }
            }
        }
    }
}

