package io.github.bonsaistudi0s.crittersandcompanions.common.mixin;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.JumpingSpiderEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Silverfish.class)
public class SilverfishMixin extends Monster {

    protected SilverfishMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "registerGoals",
            at = @At("TAIL")
    )
    private void cac$addFleeFromJumpingSpider(CallbackInfo ci) {
        var self = (Silverfish) (Object) this;
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(
                self,
                JumpingSpiderEntity.class,
                8.0F,
                1.0D,
                1.2D
        ));
    }
}
