package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.entity.StickBugEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractIllager.class)
public abstract class AbstractIllagerMixin extends Mob {

    protected AbstractIllagerMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "registerGoals",
            at = @At("TAIL")
    )
    private void cac$addFleeFromStickBug(CallbackInfo ci) {
        var self = (AbstractIllager) (Object) this;
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(
                self,
                StickBugEntity.class,
                8.0F,
                1.0D,
                1.2D
        ));
    }
}
