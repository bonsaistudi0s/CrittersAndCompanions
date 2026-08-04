package io.github.bonsaistudi0s.crittersandcompanions.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;

@Mixin(ChestBlock.class)
public class ChestBlockMixin {

    @Inject(method = "isCatSittingOnChest", at = @At("RETURN"), cancellable = true)
    private static void crittersandcompanions$isTamableSittingOnChest(LevelAccessor level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        var catAlreadySittingOnChest = cir.getReturnValue();
        if (catAlreadySittingOnChest) {
            return;
        }

        var list = level.getEntitiesOfClass(TamableAnimal.class, new AABB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1));
        for (var tamable : list) {
            if (!tamable.isInSittingPose()) {
                continue;
            }

            var namespace = BuiltInRegistries.ENTITY_TYPE.getKey(tamable.getType()).getNamespace();
            if (namespace.equals(CrittersAndCompanions.MODID)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
