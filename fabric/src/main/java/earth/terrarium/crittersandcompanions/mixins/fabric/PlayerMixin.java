package earth.terrarium.crittersandcompanions.mixins.fabric;

import earth.terrarium.crittersandcompanions.common.handler.PlayerHandler;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    public void crittersandcompanions$onPlayerTick(CallbackInfo ci) {
        PlayerHandler.onPlayerTick((Player) (Object) this);
    }
}
