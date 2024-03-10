package earth.terrarium.crittersandcompanions.common.mixin;

import earth.terrarium.crittersandcompanions.common.item.PearlNecklaceItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

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
            this.targetConditions.selector(targetPredicate != null ?
                    targetPredicate.and(target -> {

                        if (target instanceof Player player) {
                            Inventory inventory = player.getInventory();

                            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                                ItemStack stack = inventory.getItem(i);
                                if (stack.getItem() instanceof PearlNecklaceItem pearlNecklaceItem) {
                                    double range = this.getFollowDistance() - (this.getFollowDistance() * ((pearlNecklaceItem.getLevel() * 20) / 100.0F));
                                    return player.position().closerThan(mob.position(), range);
                                }
                            }
                        }
                        return true;
                    }) : null
            );
        }
    }
}
