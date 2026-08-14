package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class TamableAnimalPanicGoal extends ModernPanicGoal {

    public TamableAnimalPanicGoal(TamableAnimal tamableAnimal, final double speedModifier, final TagKey<DamageType> panicCausingDamageTypes) {
        super(tamableAnimal, speedModifier, panicCausingDamageTypes);
    }

    public TamableAnimalPanicGoal(TamableAnimal tamableAnimal, final double speedModifier) {
        super(tamableAnimal, speedModifier);
    }

    public void tick() {
        var tamableAnimal = (TamableAnimal) mob;
        var owner = tamableAnimal.getOwner();

        var unableToMoveToOwner = tamableAnimal.isOrderedToSit() || tamableAnimal.isPassenger() || tamableAnimal.isLeashed() || tamableAnimal.getOwner() != null && tamableAnimal.getOwner().isSpectator();
        var shouldTryTeleportToOwner = owner != null && tamableAnimal.distanceToSqr(owner) >= 144.0;

        if (!unableToMoveToOwner && shouldTryTeleportToOwner) {
            tryToTeleportToOwner();
        }

        super.tick();
    }

    private void tryToTeleportToOwner() {
        var tamableAnimal = (TamableAnimal) mob;
        var owner = tamableAnimal.getOwner();

        if (owner == null) {
            return;
        }

        this.teleportToAroundBlockPos(owner.blockPosition());
    }

    private void teleportToAroundBlockPos(BlockPos blockPos) {
        var tamableAnimal = (TamableAnimal) mob;
        
        for (var i = 0; i < 10; i++) {
            var j = tamableAnimal.getRandom().nextIntBetweenInclusive(-3, 3);
            var k = tamableAnimal.getRandom().nextIntBetweenInclusive(-3, 3);
            if (Math.abs(j) >= 2 || Math.abs(k) >= 2) {
                var l = tamableAnimal.getRandom().nextIntBetweenInclusive(-1, 1);
                if (this.maybeTeleportTo(blockPos.getX() + j, blockPos.getY() + l, blockPos.getZ() + k)) {
                    return;
                }
            }
        }
    }

    private boolean maybeTeleportTo(int i, int j, int k) {
        var tamableAnimal = (TamableAnimal) mob;

        if (!this.canTeleportTo(new BlockPos(i, j, k))) {
            return false;
        } else {
            tamableAnimal.moveTo(i + 0.5, j, k + 0.5, tamableAnimal.getYRot(), tamableAnimal.getXRot());
            tamableAnimal.getNavigation().stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos blockPos) {
        var tamableAnimal = (TamableAnimal) mob;

        var pathType = WalkNodeEvaluator.getBlockPathTypeStatic(tamableAnimal.level(), blockPos.mutable());
        if (pathType != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            var blockState = tamableAnimal.level().getBlockState(blockPos.below());
            if (!(tamableAnimal instanceof FlyingAnimal) && blockState.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                var blockPos2 = blockPos.subtract(tamableAnimal.blockPosition());
                return tamableAnimal.level().noCollision(tamableAnimal, tamableAnimal.getBoundingBox().move(blockPos2));
            }
        }
    }
}
