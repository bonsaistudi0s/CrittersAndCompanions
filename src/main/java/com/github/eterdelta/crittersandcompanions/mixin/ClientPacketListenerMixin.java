package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.client.sound.DragonflySoundInstance;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
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
