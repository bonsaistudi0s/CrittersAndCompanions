package io.github.bonsaistudi0s.crittersandcompanions.common.mixin;

import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.item.PearlNecklaceItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin<T extends LivingEntity> extends TargetGoal {

    @Accessor
    abstract TargetingConditions getTargetConditions();

    protected NearestAttackableTargetGoalMixin(Mob mob, boolean mustSee) {
        super(mob, mustSee);
    }

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V")
    private void onInit(Mob mob, Class<T> targetType, int randomInterval, boolean mustSee, boolean mustReach, Predicate<LivingEntity> targetPredicate, CallbackInfo callback) {
        if (targetPredicate != null) {
            getTargetConditions().selector(
                    targetPredicate.and(target -> PearlNecklaceItem.getWearing(target)
                            .map(PearlNecklaceItem::getLevel)
                            .map(level -> CACCommonConfig.HANDLER.instance().necklace.rangeDebuff(mob.getType(), level))
                            .filter(it -> it > 0)
                            .map(debuff -> getFollowDistance() - (getFollowDistance() * debuff))
                            .map(range -> target.position().closerThan(mob.position(), range))
                            .orElse(true)
                    )
            );
        }
    }
}
