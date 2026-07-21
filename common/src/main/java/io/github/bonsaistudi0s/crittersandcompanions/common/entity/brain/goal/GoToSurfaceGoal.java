package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.OtterEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GoToSurfaceGoal extends Goal {
    private final OtterEntity mob;
    private final int timeoutTime;
    private boolean goingLand;
    private Vec3 targetPos;
    private int timeoutTimer;

    private int stuckTicks;
    private double lastDist = Double.MAX_VALUE;

    public GoToSurfaceGoal(OtterEntity mob, int timeoutTime) {
        this.mob = mob;
        this.timeoutTime = timeoutTime;
        this.timeoutTimer = timeoutTime;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean isReadyToFloat() {
        BlockPos eye = BlockPos.containing(mob.getX(), mob.getEyeY() + 0.25, mob.getZ());
        return !mob.isUnderWater() && mob.level().getBlockState(eye).isAir() && mob.level().getFluidState(eye.below()).is(FluidTags.WATER);
    }

    private Vec3 findSurfacePosStraightUp() {
        BlockPos.MutableBlockPos curr = new BlockPos.MutableBlockPos(Mth.floor(mob.getX()), Mth.floor(mob.getEyeY()), Mth.floor(mob.getZ()));

        boolean waterInSight = false;
        for (int i = 0; i < 40; i++) {
            BlockPos pos = curr.above(i);
            if (mob.level().getFluidState(pos).is(FluidTags.WATER)) {
                waterInSight = true;
                continue;
            }

            if (waterInSight && mob.level().getBlockState(pos).isAir()) {
                return Vec3.atCenterOf(pos).add(0.0D, 0.25D, 0.0D);
            }

        }

        return null;
    }

    @Override
    public boolean canUse() {
        return mob.isAlive() && mob.needsSurface() && !mob.onGround() && !mob.isFloating();
    }

    private void searchTargetPos() {
        if (mob.isInWater() && mob.isFood(mob.getMainHandItem())) {
            Vec3 surface = findSurfacePosStraightUp();
            if (surface != null) {
                this.targetPos = surface;
                return;
            }

        }

        Vec3 surface = findSurfacePosStraightUp();
        if (surface != null) {
            this.targetPos = surface;
            return;
        }

        this.targetPos = findAirPosition();
    }

    @Override
    public void start() {
        this.stuckTicks = 0;
        this.lastDist = Double.MAX_VALUE;
        searchTargetPos();
    }

    @Override
    public void tick() {
        // Embed goals sometimes computee client sided ticks due to a sync error, although this happens in singleplayer
        // it's better to encapsulate the method
        if (mob.level().isClientSide) {
            return;
        }

        if (isReadyToFloat()) {
            mob.setDeltaMovement(mob.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
            mob.setYya(0.0F);
            mob.setSpeed(0.0F);

            mob.startFloating(mob.getRandom().nextInt(80, 201));

            this.stop();

            return;
        }

        if (this.targetPos == null || !mob.level().getBlockState(BlockPos.containing(this.targetPos)).isAir()) {
            searchTargetPos();
            this.tickTimeout();
            return;
        }

        mob.getLookControl().setLookAt(this.targetPos.x(), this.targetPos.y(), this.targetPos.z(), 85.0F, 85.0F);
        // When the entity goes up it sometimes has a LOT of velocity, so this may help (I hope)
        mob.getNavigation().moveTo(this.targetPos.x(), this.targetPos.y(), this.targetPos.z(), 0.5);

        double dx = this.targetPos.x() - mob.getX();
        double dy = this.targetPos.y() - mob.getEyePosition().y();
        double dz = this.targetPos.z() - mob.getZ();

        double horiz = dx * dx + dz * dz;
        // Pushes the entity to Y+ in case it's near the surface 'line'
        boolean navDone = mob.getNavigation().isDone();
        double basePush = 0.02D;
        if ((dy > 0.0D) && mob.isUnderWater()) {
            if (navDone || horiz <= 0.25D) {
                Vec3 v = mob.getDeltaMovement();
                mob.setDeltaMovement(v.x * 0.6D, v.y + 0.01D, v.z * 0.6D);
            } else if (horiz <= 9.0D) {
                mob.push(0.0D, basePush, 0.0D);
            }

        }

        // Hardcoded velocity clamp near the desired surface as high velocity tends to push the otter far away from the
        // relevant pos, thus making it fly in the air
        double absDy = Math.abs(dy);
        if (absDy <= 0.85725D) {
            mob.setDeltaMovement(mob.getDeltaMovement().scale(0.35D));
        }

        // Starts the floating state
        if (absDy <= 0.1D && isReadyToFloat()) {
            mob.setDeltaMovement(mob.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
            mob.setYya(0.0F);
            mob.setSpeed(0.0F);
            mob.startFloating(mob.getRandom().nextInt(80, 201));

            this.stop();

            return;
        }

        double dist = dx * dx + dy * dy + dz * dz;
        if (dist > this.lastDist - 0.0001D) {
            this.stuckTicks++;
        } else {
            this.stuckTicks = 0;
        }

        this.lastDist = dist;

        // Fallback if the entity isn't near of the desired pos
        if ((navDone && dist > 2.25D) || this.stuckTicks > 20) {
            if (isReadyToFloat()) {
                mob.startFloating(mob.getRandom().nextInt(80, 201));
                this.stop();
                return;
            }

            searchTargetPos();
            this.tickTimeout();
            this.stuckTicks = 0;
        }

    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private void tickTimeout() {
        if (this.timeoutTimer % 2 == 0) {
            ((ServerLevel) mob.level()).sendParticles(ParticleTypes.BUBBLE, mob.getRandomX(0.6D), mob.getY(), mob.getRandomZ(0.6D), 2, 0.0D, 0.1D, 0.0D, 0.0D);
        }
        if (this.timeoutTimer <= 0) {
            mob.playSound(CACSounds.OTTER_AMBIENT.get(), 1.0F, 0.3F);
            mob.rejectFood();
            this.stop();
            return;
        }
        --this.timeoutTimer;
    }

    @Override
    public void stop() {
        mob.setNeedsSurface(false);
        mob.getNavigation().stop();
        this.timeoutTimer = this.timeoutTime;

        this.targetPos = null;
        this.stuckTicks = 0;
        this.lastDist = Double.MAX_VALUE;
    }

    private Vec3 findAirPosition() {
        Iterable<BlockPos> blocksInRadius = BlockPos.betweenClosed(Mth.floor(mob.getX() - 1.0D), Mth.floor(mob.getBlockY()), Mth.floor(mob.getZ() - 1.0D), Mth.floor(mob.getX() + 1.0D), Mth.floor(mob.getY() + 32.0D), Mth.floor(mob.getZ() + 1.0D));

        for (BlockPos pos : blocksInRadius) {
            if (mob.level().getBlockState(pos).isAir()) {
                return Vec3.atBottomCenterOf(pos);
            }
        }

        return null;
    }
}
