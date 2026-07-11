package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

import java.util.function.Predicate;

public class FlyingAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

    public FlyingAvoidEntityGoal(PathfinderMob mob, Class<T> entityClassToAvoid, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier) {
        super(mob, entityClassToAvoid, maxDistance, walkSpeedModifier, sprintSpeedModifier);
    }

    public FlyingAvoidEntityGoal(PathfinderMob mob, Class<T> entityClassToAvoid, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier, Predicate<LivingEntity> predicateOnAvoidEntity) {
        super(mob, entityClassToAvoid, maxDistance, walkSpeedModifier, sprintSpeedModifier, predicateOnAvoidEntity);
    }

    @Override
    public boolean canUse() {
        this.toAvoid = this.mob.level().getNearestEntity(
                this.mob.level().getEntitiesOfClass(this.avoidClass, this.mob.getBoundingBox().inflate(this.maxDist, this.maxDist, this.maxDist), (livingEntity) -> true),
                this.avoidEntityTargeting,
                this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ()
        );

        if (this.toAvoid == null) {
            return false;
        }

        var mobPos = this.mob.position();
        var targetPos = this.toAvoid.position();

        var awayDirection = mobPos.subtract(targetPos).normalize();
        var fleePos = mobPos.add(awayDirection.scale(10.0));

        if (this.toAvoid.distanceToSqr(fleePos) < this.toAvoid.distanceToSqr(this.mob)) {
            return false;
        }

        this.path = this.pathNav.createPath(fleePos.x, fleePos.y, fleePos.z, 0);
        return this.path != null;
    }
}
