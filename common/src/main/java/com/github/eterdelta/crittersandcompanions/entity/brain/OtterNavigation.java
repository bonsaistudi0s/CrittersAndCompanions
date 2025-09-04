package com.github.eterdelta.crittersandcompanions.entity.brain;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Custom hybrid navigator (that works for both liquid and ground sources)
 * A chunk of the implementation is derived from the GroundNavigator from Companions!
 * https://github.com/Xylonity/Companions/blob/v1.20.1/common/src/main/java/dev/xylonity/companions/common/ai/navigator/GroundNavigator.java#L48
 */
public class OtterNavigation extends AmphibiousPathNavigation {

    private static final float EPSILON = 1.0E-8F;

    private static final double PROGRESS_MIN2 = 0.35D * 0.35D;
    private static final int PROGRESS_TICKS = 18;

    private final Cache<BlockPos, Boolean> cache = CacheBuilder.newBuilder()
            .maximumSize(12000)
            .expireAfterAccess(5, TimeUnit.SECONDS)
            .build();

    private Vec3 lastCheckPos = Vec3.ZERO;
    private int lastCheckTick = 0;
    private int stuckReplans = 0;
    private int jumpCooldown = 0;

    public OtterNavigation(Mob mob, Level level) {
        super(mob, level);
        this.setCanFloat(true);
    }

    @Override
    protected PathFinder createPathFinder(int nodes) {
        AmphibiousNodeEvaluator evaluator = new AmphibiousNodeEvaluator(true);
        evaluator.setCanPassDoors(false);
        this.nodeEvaluator = evaluator;
        return new PathFinder(this.nodeEvaluator, nodes);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        if (!this.level.getBlockState(pos).isAir()) {
            return super.isStableDestination(pos);
        }

        BlockState below = this.level.getBlockState(pos.below());
        if (!(!below.isAir() || below.getFluidState().is(FluidTags.WATER))) {
            return false;
        }

        return this.level.getBlockState(pos).isAir() && this.level.getBlockState(pos.above()).isAir();
    }

    /**
     * Core loop for following the current path. Attempts shortcuts, moves towards the next node and handles
     * the entity jumping (the latter killed me ngl)
     */
    @Override
    protected void followThePath() {
        // If there is no path
        if (this.path == null || this.path.isDone()) return;

        Vec3 entityPos = this.getTempMobPos();
        int nextIdx = path.getNextNodeIndex();
        double yFloor = Math.floor(entityPos.y);

        // Checks if there are any more nodes remaining on the same Y
        int lastIdx = nextIdx;
        while (lastIdx < path.getNodeCount() && path.getNode(lastIdx).y == yFloor) {
            lastIdx++;
        }

        // Computes path to the approx next node
        for (int i = lastIdx - 1; i > nextIdx; i--) {
            if (catchF(entityPos, path.getEntityPosAtNode(this.mob, i))) {
                path.setNextNodeIndex(i);
                break;
            }

        }

        // If the entity is very close to the next node (or on an elevation change), advance the path index
        if (hasReached(path, 0.8F) || (isAtElevationChange(path) && hasReached(path, 1.0F))) {
            path.advance();
        }

        // If we still have a node to reach, instruct the mob to move over there
        if (path.isDone()) return;

        // Move the entity
        Vec3 target = path.getNextEntityPos(this.mob);
        this.mob.getMoveControl().setWantedPosition(target.x, target.y, target.z, this.speedModifier);

        // Jump fallback (if the entity is stuck)
        if (jumpCooldown > 0) {
            jumpCooldown--;
        }

        if (!this.mob.isInWater() && this.mob.onGround() && this.mob.horizontalCollision && jumpCooldown == 0) {
            // The dir towards the node (not usin getDirection as it can 'lie' depending on the azimuth of the entity)
            Vec3 dir = new Vec3(target.x - this.mob.getX(), 0.0, target.z - this.mob.getZ());
            if (dir.lengthSqr() > 1.0E-4) dir = dir.normalize();

            // Cell right in front of the entity
            double reach = this.mob.getBbWidth() * 0.5D + 0.6D;
            BlockPos front = BlockPos.containing(this.mob.getX() + dir.x * reach, Math.floor(this.mob.getY()), this.mob.getZ() + dir.z * reach);

            // Real height of the block (solid) in front of the entity
            VoxelShape shape =  this.level.getBlockState(front).getCollisionShape(this.level, front);
            double h = shape.isEmpty() ? 0.0D : shape.max(Direction.Axis.Y);

            // How much free space aboce the entity (two blocks max)
            BlockPos head = front.above();
            boolean headClear = this.level.getBlockState(head).getCollisionShape(this.level, head).isEmpty();
            boolean head2Clear = this.level.getBlockState(head.above()).getCollisionShape(this.level, head.above()).isEmpty();

            // Only jump if the obstacle is "jumpable" and theres space above
            if (h > 0.01D && h <= 1.2D && headClear && head2Clear) {
                this.mob.getJumpControl().jump();
                jumpCooldown = 6;
            } else {
                // Fallback for small perpen sidestep, so it goes around the tight corners
                perpendicularSideS(dir);
            }

        }

        if (this.mob.isInWater()) {
            // Fallback when the entity collides underwater
            if (this.mob.horizontalCollision && damp()) return;

            if (this.level.getFluidState(BlockPos.containing(this.mob.getEyePosition())).is(FluidTags.WATER)) {
                if (target.y > entityPos.y) {
                    // Forces a push to the higher Ys when the next node is in a higher Y than the current  one
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, 0.03D, 0.0D));
                }
            }

            if (this.mob.onGround() && this.mob.horizontalCollision && jumpCooldown == 0) {
                BlockPos nose = this.mob.blockPosition().relative(this.mob.getDirection());
                VoxelShape shape = this.level.getBlockState(nose).getCollisionShape(this.level, nose);
                double shadeHeight = shape.isEmpty() ? 0.0D : shape.max(Direction.Axis.Y);
                if (shadeHeight > 0.01D && shadeHeight <= 1.2D
                        && this.level.getBlockState(nose.above()).getCollisionShape(this.level, nose.above()).isEmpty()
                        && this.level.getBlockState(nose.above(2)).getCollisionShape(this.level, nose.above(2)).isEmpty()) {
                    this.mob.getJumpControl().jump();
                    jumpCooldown = 6;
                }

            }

        }

        if (this.mob.tickCount - this.lastCheckTick >= PROGRESS_TICKS) {
            Vec3 curr = this.mob.position();
            if (curr.distanceToSqr(this.lastCheckPos) < PROGRESS_MIN2) {
                BlockPos targetPos = this.getTargetPos();
                if (targetPos != null) {
                    this.recomputePath();
                }

                if (++this.stuckReplans >= 3 && this.path != null && !this.path.isDone()) {
                    Vec3 dir = new Vec3(target.x - curr.x, 0.0, target.z - curr.z);
                    if (dir.lengthSqr() > 1.0E-4) {
                        dir = dir.normalize();
                    }

                    perpendicularSideS(dir);
                    this.stuckReplans = 0;
                }

            } else {
                this.stuckReplans = 0;
            }

            this.lastCheckPos = curr;
            this.lastCheckTick = this.mob.tickCount;
        }

    }

    private boolean hasReached(Path path, float threshold) {
        Vec3 pos = path.getNextEntityPos(this.mob);

        if (Math.abs(this.mob.getX() - pos.x) >= threshold) return false;
        if (Math.abs(this.mob.getZ() - pos.z) >= threshold) return false;

        return Math.abs(this.mob.getY() - pos.y) <= 1.001D;
    }

    private boolean isAtElevationChange(Path path) {
        int idx = path.getNextNodeIndex();
        int end = Math.min(path.getNodeCount(), idx + Mth.ceil(this.mob.getBbWidth() * 0.5F) + 1);
        int y = path.getNode(idx).y;

        for (int i = idx + 1; i < end; i++) {
            if (path.getNode(i).y != y) {
                return true;
            }

        }

        return false;
    }

    /**
     * 3D DDA algorithm to check if a straight line to a node is clear
     */
    private boolean catchF(Vec3 from, Vec3 to) {
        Vec3 vec = to.subtract(from);

        float maxT = (float) vec.length();
        if (maxT < 1.0E-6F) return true; // too close to worry about

        // Normalized direction
        float dx = (float) (vec.x / maxT);
        float dy = (float) (vec.y / maxT);
        float dz = (float) (vec.z / maxT);

        int currentX = Mth.floor(from.x);
        int currentY = Mth.floor(from.y);
        int currentZ = Mth.floor(from.z);

        // Compute step for each axis
        int stepX;
        int stepY;
        int stepZ;
        float tNextX;
        float tNextY;
        float tNextZ;
        float tDeltaX;
        float tDeltaY;
        float tDeltaZ;

        // X axis
        if (Math.abs(dx) < EPSILON) {
            tDeltaX = Float.POSITIVE_INFINITY;
            tNextX = Float.POSITIVE_INFINITY;
            stepX = 0;
        } else {
            stepX = dx > 0 ? 1 : -1;
            float voxelBoundaryX = stepX > 0 ? Mth.floor(from.x) + 1 : Mth.floor(from.x);
            tDeltaX = 1.0F / Math.abs(dx);
            tNextX = (float) ((voxelBoundaryX - from.x) / dx);
        }

        // Y axis
        if (Math.abs(dy) < EPSILON) {
            tDeltaY = Float.POSITIVE_INFINITY;
            tNextY = Float.POSITIVE_INFINITY;
            stepY = 0;
        } else {
            stepY = dy > 0 ? 1 : -1;
            float voxelBoundaryY = stepY > 0 ? Mth.floor(from.y) + 1 : Mth.floor(from.y);
            tDeltaY = 1.0F / Math.abs(dy);
            tNextY = (float) ((voxelBoundaryY - from.y) / dy);
        }

        // Z axis
        if (Math.abs(dz) < EPSILON) {
            tDeltaZ = Float.POSITIVE_INFINITY;
            tNextZ = Float.POSITIVE_INFINITY;
            stepZ = 0;
        } else {
            stepZ = dz > 0 ? 1 : -1;
            float voxelBoundaryZ = stepZ > 0 ? Mth.floor(from.z) + 1 : Mth.floor(from.z);
            tDeltaZ = 1.0F / Math.abs(dz);
            tNextZ = (float) ((voxelBoundaryZ - from.z) / dz);
        }

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        float t = 0.0F;

        // March along the ray until we exceed the target distance
        while (t <= maxT) {
            if (tNextX < tNextY) {
                if (tNextX < tNextZ) {
                    currentX += stepX;
                    t = tNextX;
                    tNextX += tDeltaX;
                }
                else {
                    currentZ += stepZ;
                    t = tNextZ;
                    tNextZ += tDeltaZ;
                }
            } else {
                if (tNextY < tNextZ) {
                    currentY += stepY;
                    t = tNextY;
                    tNextY += tDeltaY;
                }
                else { currentZ += stepZ;
                    t = tNextZ;
                    tNextZ += tDeltaZ;
                }

            }

            pos.set(currentX, currentY, currentZ);
            BlockPos immutablePos = pos.immutable();

            // Caches nodes to avoid recomputing them again
            Boolean isPathfindable = cache.getIfPresent(immutablePos);
            if (isPathfindable == null) {
                BlockState blockStatet = this.level.getBlockState(pos);
                isPathfindable = blockStatet.isPathfindable(this.level, pos, PathComputationType.LAND);
                cache.put(immutablePos, isPathfindable);
            }
            if (!isPathfindable)
                return false;

            // Also rejects if the block's path type is not okie dokie
            BlockPathTypes pathType = this.nodeEvaluator.getBlockPathType(this.level, currentX, currentY, currentZ, this.mob);
            float malus = this.mob.getPathfindingMalus(pathType);

            if (malus < 0.0F
                    || malus >= 8.0F
                    || pathType == BlockPathTypes.DAMAGE_FIRE
                    || pathType == BlockPathTypes.DANGER_FIRE
                    || pathType == BlockPathTypes.DAMAGE_OTHER)
                return false;

        }

        return true;
    }

    private void perpendicularSideS(Vec3 dirNorm) {
        double px = -dirNorm.z; // perpen
        double pz = dirNorm.x; // perpen
        double side = this.mob.getRandom().nextBoolean() ? 1.0D : -1.0D;

        Vec3 curr = this.mob.position();
        BlockPos step = BlockPos.containing(curr.x + px * side * 1.5D, curr.y, curr.z + pz * side * 1.5D);
        if (this.isStableDestination(step)) {
            this.moveTo(step.getX() + 0.5D, step.getY(), step.getZ() + 0.5D, 1.1D);
        }

    }

    private boolean damp() {
        BlockPos currentPos = this.mob.blockPosition();
        if (!this.level.getFluidState(currentPos).is(FluidTags.WATER)) return false;

        for (Direction dir : new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            for (int i = 1; i <= 2; i++) {
                BlockPos edge = currentPos.relative(dir, i);
                if (!(this.level.getFluidState(edge).is(FluidTags.WATER) && this.level.getBlockState(edge.above()).isAir() && this.level.getBlockState(edge.above(2)).isAir())) continue;

                BlockPos land = edge.relative(dir);
                if (this.level.getBlockState(land).isAir() && this.level.getBlockState(land.below()).isSolid() && this.isStableDestination(land)) {
                    this.moveTo(land.getX() + 0.5D, land.getY(), land.getZ() + 0.5D, Math.max(1.0D, this.speedModifier));
                    return true;
                }
            }

        }

        return false;
    }

}
