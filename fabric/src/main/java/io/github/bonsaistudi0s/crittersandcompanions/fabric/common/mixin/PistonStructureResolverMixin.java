package io.github.bonsaistudi0s.crittersandcompanions.fabric.common.mixin;

import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.bonsaistudi0s.crittersandcompanions.common.block.SeaBunnySlimeBlock;

@Mixin(PistonStructureResolver.class)
public class PistonStructureResolverMixin {

    @Inject(at = @At("HEAD"), method = "isSticky(Lnet/minecraft/world/level/block/state/BlockState;)Z", cancellable = true)
    private static void injectIsSticky(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof SeaBunnySlimeBlock block) {
            cir.setReturnValue(block.isStickyBlock(state));
        }
    }


    @Inject(at = @At("HEAD"), method = "canStickToEachOther(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;)Z", cancellable = true)
    private static void injectCanStickToEachOther(BlockState a, BlockState b, CallbackInfoReturnable<Boolean> cir) {
        if (a.getBlock() instanceof SeaBunnySlimeBlock block) {
            cir.setReturnValue(block.canStickTo(a, b));
        }
    }

}
