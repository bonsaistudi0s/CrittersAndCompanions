package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.OtterEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;
import java.util.List;

public class SearchFoodGoal extends Goal {

    private final OtterEntity mob;

    public SearchFoodGoal(OtterEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!mob.getMainHandItem().isEmpty()) {
            return false;
        } else {
            List<ItemEntity> itemsInRadius = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> mob.wantsToPickUp(itemEntity.getItem()) && !mob.rejectedItem(itemEntity)));
            return !itemsInRadius.isEmpty();
        }
    }

    @Override
    public void tick() {
        List<ItemEntity> itemsInRadius = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> mob.wantsToPickUp(itemEntity.getItem()) && !mob.rejectedItem(itemEntity)));
        ItemStack handStack = mob.getMainHandItem();
        if (handStack.isEmpty() && !itemsInRadius.isEmpty()) {
            Path path = mob.getNavigation().createPath(itemsInRadius.get(0), 0);
            mob.getNavigation().moveTo(path, 1.0D);
        }
    }

    @Override
    public void start() {
        List<ItemEntity> itemsInRadius = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> mob.wantsToPickUp(itemEntity.getItem()) && !mob.rejectedItem(itemEntity)));
        if (!itemsInRadius.isEmpty()) {
            Path path = mob.getNavigation().createPath(itemsInRadius.get(0), 0);
            mob.getNavigation().moveTo(path, 1.0D);
        }
    }
}
