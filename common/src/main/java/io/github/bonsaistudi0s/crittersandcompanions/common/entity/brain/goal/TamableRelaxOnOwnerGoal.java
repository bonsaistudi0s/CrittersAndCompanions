package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TamableRelaxOnOwnerGoal<T extends TamableAnimal> extends Goal {
    private final T mob;
    private final Predicate<T> isLying;
    private final Consumer<Boolean> setLying;
    @Nullable
    private Player ownerPlayer;
    @Nullable
    private BlockPos goalPos;

    public TamableRelaxOnOwnerGoal(T mob, Predicate<T> isLying, Consumer<Boolean> setLying) {
        this.mob = mob;
        this.isLying = isLying;
        this.setLying = setLying;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    private boolean isLying(T entity) {
        return this.isLying.test(entity);
    }
    
    private void setLying(boolean lying) {
        this.setLying.accept(lying);
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isTame()) {
            return false;
        } else if (this.mob.isOrderedToSit()) {
            return false;
        } else {
            LivingEntity livingEntity = this.mob.getOwner();
            if (livingEntity instanceof Player) {
                this.ownerPlayer = (Player)livingEntity;
                if (!livingEntity.isSleeping()) {
                    return false;
                }

                if (this.mob.distanceToSqr(this.ownerPlayer) > 100.0D) {
                    return false;
                }

                BlockPos blockPos = this.ownerPlayer.blockPosition();
                BlockState blockState = this.mob.level().getBlockState(blockPos);
                if (blockState.is(BlockTags.BEDS)) {
                    this.goalPos = blockState.getOptionalValue(BedBlock.FACING).map((direction) -> blockPos.relative(direction.getOpposite())).orElseGet(() -> new BlockPos(blockPos));
                    return !this.spaceIsOccupied();
                }
            }

            return false;
        }
    }

    private boolean spaceIsOccupied() {
        //noinspection unchecked
        var mobClass = (Class<T>) this.mob.getClass();
        for(T entity : this.mob.level().getEntitiesOfClass(mobClass, (new AABB(this.goalPos)).inflate(2.0D))) {
            if (entity != this.mob && this.isLying(entity)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isTame() && !this.mob.isOrderedToSit() && this.ownerPlayer != null && this.ownerPlayer.isSleeping() && this.goalPos != null && !this.spaceIsOccupied();
    }

    @Override
    public void start() {
        if (this.goalPos != null) {
            this.mob.setInSittingPose(false);
            this.mob.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.1D);
        }
    }

    @Override
    public void stop() {
        this.setLying(false);
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.ownerPlayer != null && this.goalPos != null) {
            this.mob.setInSittingPose(false);
            this.mob.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.1D);
            if (this.mob.distanceToSqr(this.ownerPlayer) < 2.5D) {
                this.setLying(true);
            } else {
                this.setLying(false);
            }
        }
    }
}
