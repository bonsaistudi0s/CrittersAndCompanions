package com.github.eterdelta.crittersandcompanions.entity.brain;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;

public class OtterNodeEvaluator extends AmphibiousNodeEvaluator {

    public OtterNodeEvaluator() {
        super(false);
    }

    @Override
    public int getNeighbors(Node[] nodes, Node $$1) {
        int walkableNeighbors = super.getNeighbors(nodes, $$1);
        PathType abovePathType = this.getCachedPathType($$1.x, $$1.y + 1, $$1.z);
        PathType pathType = this.getCachedPathType($$1.x, $$1.y, $$1.z);
        int yRange;
        if (this.mob.getPathfindingMalus(abovePathType) >= 0.0F && pathType != PathType.STICKY_HONEY) {
            yRange = this.mob.isUnderWater() && (pathType == PathType.WATER || pathType == PathType.WATER_BORDER)
                    ? 32
                    : Mth.floor(Math.max(1.0F, this.mob.maxUpStep()));
        } else {
            yRange = 0;
        }

        double $$7 = this.getFloorLevel(new BlockPos($$1.x, $$1.y, $$1.z));
        Node $$8 = this.findAcceptedNode($$1.x, $$1.y + 1, $$1.z, Math.max(0, yRange - 1), $$7, Direction.UP, pathType);
        Node $$9 = this.findAcceptedNode($$1.x, $$1.y - 1, $$1.z, yRange, $$7, Direction.DOWN, pathType);
        if (this.isNeighborValid($$8, $$1)) {
            nodes[walkableNeighbors++] = $$8;
        }

        if (this.isNeighborValid($$9, $$1) && pathType != PathType.TRAPDOOR) {
            nodes[walkableNeighbors++] = $$9;
        }

        return walkableNeighbors;
    }

}
