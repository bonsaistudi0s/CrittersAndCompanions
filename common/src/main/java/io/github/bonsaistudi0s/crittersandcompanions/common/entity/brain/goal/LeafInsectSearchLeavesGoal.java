package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.LeafInsectEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.EnumSet;

public class LeafInsectSearchLeavesGoal extends Goal {

    private final LeafInsectEntity mob;

    public LeafInsectSearchLeavesGoal(LeafInsectEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!mob.getMainHandItem().isEmpty()) {
            return false;
        } else {
            var itemsInRadius = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> mob.wantsToPickUp(itemEntity.getItem())));
            return !itemsInRadius.isEmpty();
        }
    }

    @Override
    public void tick() {
        var itemsInRadius = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> mob.wantsToPickUp(itemEntity.getItem())));
        var handStack = mob.getMainHandItem();
        if (handStack.isEmpty() && !itemsInRadius.isEmpty()) {
            var path = mob.getNavigation().createPath(itemsInRadius.get(0), 0);
            mob.getNavigation().moveTo(path, 1.0D);
        }
    }

    @Override
    public void start() {
        var itemsInRadius = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> mob.wantsToPickUp(itemEntity.getItem())));
        if (!itemsInRadius.isEmpty()) {
            var path = mob.getNavigation().createPath(itemsInRadius.get(0), 0);
            mob.getNavigation().moveTo(path, 1.0D);
        }
    }
}
