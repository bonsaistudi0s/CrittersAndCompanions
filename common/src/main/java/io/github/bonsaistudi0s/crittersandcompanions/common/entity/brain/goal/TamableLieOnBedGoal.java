package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TamableLieOnBedGoal<T extends TamableAnimal> extends MoveToBlockGoal {
    private final T mob;
    private final Predicate<T> isLying;
    private final Consumer<Boolean> setLying;
    private final int cooldownTime;

    public TamableLieOnBedGoal(T mob, double speedModifier, int searchRange, Predicate<T> isLying, Consumer<Boolean> setLying, int cooldownTime) {
        super(mob, speedModifier, searchRange, 6);
        this.mob = mob;
        this.isLying = isLying;
        this.setLying = setLying;
        this.cooldownTime = cooldownTime;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    public TamableLieOnBedGoal(T mob, double speedModifier, int searchRange, Predicate<T> isLying, Consumer<Boolean> setLying) {
        this(mob, speedModifier, searchRange, isLying, setLying, 1200);
    }

    private boolean isLying() {
        return this.isLying.test(this.mob);
    }

    private void setLying(boolean lying) {
        this.setLying.accept(lying);
    }

    @Override
    public boolean canUse() {
        return this.mob.isTame() && !this.mob.isOrderedToSit() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.mob.setInSittingPose(false);
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !this.mob.isOrderedToSit();
    }

    @Override
    protected int nextStartTick(PathfinderMob creature) {
        return 40;
    }

    @Override
    public void stop() {
        super.stop();
        this.setLying(false);
        this.nextStartTick = adjustedTickDelay(this.cooldownTime);
    }

    @Override
    public void tick() {
        super.tick();
        this.mob.setInSittingPose(false);

        var currentPos = this.mob.blockPosition();
        var onBed = this.isValidTarget(this.mob.level(), currentPos) || this.isValidTarget(this.mob.level(), currentPos.below());
        
        if (onBed) {
            this.mob.getNavigation().stop();
            this.setLying(true);
        } else if (!this.isReachedTarget()) {
            this.setLying(false);
        } else if (!this.isLying()) {
            this.setLying(true);
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        return level.isEmptyBlock(pos.above()) && level.getBlockState(pos).is(BlockTags.BEDS);
    }
}
