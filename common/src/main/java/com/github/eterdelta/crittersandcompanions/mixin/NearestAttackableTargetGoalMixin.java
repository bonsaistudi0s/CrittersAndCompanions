package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.item.PearlNecklaceItem;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin<T extends LivingEntity> extends TargetGoal {

    @Shadow
    protected TargetingConditions targetConditions;

    protected NearestAttackableTargetGoalMixin(Mob mob, boolean mustSee) {
        super(mob, mustSee);
    }

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V")
    private void onInit(Mob mob, Class<T> targetType, int randomInterval, boolean mustSee, boolean mustReach, Predicate<LivingEntity> targetPredicate, CallbackInfo callback) {
        if (mob instanceof Drowned || mob instanceof Guardian) {
            this.targetConditions.selector(targetPredicate != null
                    ? targetPredicate.and(target -> PearlNecklaceItem.getWearing(target)
                    .map(PearlNecklaceItem::getLevel)
                    .map(level -> getFollowDistance() - (getFollowDistance() * ((level * 20) / 100.0F)))
                    .map(range -> target.position().closerThan(mob.position(), range))
                    .orElse(true))
                    : null
            );
        }
    }
}
