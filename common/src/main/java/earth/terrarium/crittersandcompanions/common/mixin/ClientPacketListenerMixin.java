package earth.terrarium.crittersandcompanions.common.mixin;

import earth.terrarium.crittersandcompanions.client.sound.DragonflySoundInstance;
import earth.terrarium.crittersandcompanions.common.entity.DragonflyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(at = @At("HEAD"), method = "postAddEntitySoundInstance")
    private void handleAddMob(Entity packetEntity, CallbackInfo callback) {
        if (packetEntity instanceof DragonflyEntity dragonflyEntity) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(new DragonflySoundInstance(dragonflyEntity));
        }
    }
}
