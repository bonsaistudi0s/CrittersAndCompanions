package earth.terrarium.crittersandcompanions.common.mixin;

import earth.terrarium.crittersandcompanions.common.registry.CACBlocks;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonStructureResolver.class)
public abstract class PistonStructureResolverMixin {

    @Shadow
    protected static boolean isSticky(BlockState par1) {
        return false;
    }

    @Inject(method = "isSticky(Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At("HEAD"), cancellable = true)
    private static void cac$isSticky(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canStickToEachOther(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At("HEAD"), cancellable = true)
    private static void cac$canStickTo(BlockState state, BlockState other, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get()) && (!other.is(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get()) && isSticky(other))) {
            cir.setReturnValue(false);
        }
        if (other.is(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get()) && (!state.is(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get()) && isSticky(state))) {
            cir.setReturnValue(false);
        }
    }
}
