package earth.terrarium.crittersandcompanions.common.mixin;

import earth.terrarium.crittersandcompanions.common.entity.RedPandaEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombifiedPiglin.class)
public abstract class ZombifiedPiglinMixin extends PathfinderMob {

    protected ZombifiedPiglinMixin(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "addBehaviourGoals")
    public void onAddBehaviourGoals(CallbackInfo callback) {
        this.goalSelector.addGoal(-1, new AvoidEntityGoal<>(this, RedPandaEntity.class, 16.0F, 2.0D, 1.5D,
                livingEntity -> ((RedPandaEntity) livingEntity).isAlert() && ((RedPandaEntity) livingEntity).isTame()));
    }
}
