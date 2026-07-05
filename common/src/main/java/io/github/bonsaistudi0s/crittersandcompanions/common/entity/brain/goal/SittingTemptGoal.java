package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class SittingTemptGoal extends Goal {

    private static final float LOOK_DISTANCE = 8.0F;
    private static final double LOOK_DISTANCE_SQR = (double) LOOK_DISTANCE * (double) LOOK_DISTANCE;

    protected final TamableAnimal mob;
    private final Predicate<ItemStack> items;
    private final TargetingConditions begTargeting;
    private final Level level;

    @Nullable
    private Player player;

    private int lookTime;

    public SittingTemptGoal(TamableAnimal mob, Predicate<ItemStack> items) {
        this.mob = mob;
        this.level = mob.level();
        this.items = items;
        setFlags(EnumSet.of(Flag.LOOK));
        this.begTargeting = TargetingConditions.forNonCombat().range(LOOK_DISTANCE);
    }

    @Override
    public boolean canUse() {
        if (!mob.isOrderedToSit()) {
            return false;
        }

        player = level.getNearestPlayer(begTargeting, mob);
        return player != null && isTemptedBy(player);
    }

    @Override
    public boolean canContinueToUse() {
        if (!player.isAlive()) {
            return false;
        } else if (mob.distanceToSqr(player) > LOOK_DISTANCE_SQR) {
            return false;
        } else {
            return lookTime > 0 && isTemptedBy(player);
        }
    }

    @Override
    public void start() {
        lookTime = adjustedTickDelay(40 + mob.getRandom().nextInt(40));
    }

    @Override
    public void stop() {
        player = null;
    }

    @Override
    public void tick() {
        mob.getLookControl().setLookAt(player.getX(), player.getEyeY(), player.getZ(), 10.0F, (float)mob.getMaxHeadXRot());
        --lookTime;
    }

    private boolean isTemptedBy(LivingEntity entity) {
        return items.test(entity.getMainHandItem()) || items.test(entity.getOffhandItem());
    }
}
