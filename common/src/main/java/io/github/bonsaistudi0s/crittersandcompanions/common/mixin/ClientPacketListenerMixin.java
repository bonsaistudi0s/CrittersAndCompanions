package io.github.bonsaistudi0s.crittersandcompanions.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.bonsaistudi0s.crittersandcompanions.client.sound.BugsWalkSoundInstance;
import io.github.bonsaistudi0s.crittersandcompanions.client.sound.DragonflySoundInstance;
import io.github.bonsaistudi0s.crittersandcompanions.client.sound.LadybugFlySoundInstance;
import io.github.bonsaistudi0s.crittersandcompanions.client.sound.SnailWalkSoundInstance;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.*;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(at = @At("HEAD"), method = "postAddEntitySoundInstance", cancellable = true)
    private void handleAddMob(Entity packetEntity, CallbackInfo callback) {
        if (packetEntity instanceof DragonflyEntity dragonflyEntity) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(new DragonflySoundInstance(dragonflyEntity));
        } else if (packetEntity instanceof LadybugEntity ladybugEntity) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(new BugsWalkSoundInstance(ladybugEntity));
            Minecraft.getInstance().getSoundManager().queueTickingSound(new LadybugFlySoundInstance(ladybugEntity));
        } else if (packetEntity instanceof RolyPolyEntity rolyPolyEntity) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(new BugsWalkSoundInstance(rolyPolyEntity));
        } else if (packetEntity instanceof StagBeetleEntity stagBeetleEntity) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(new BugsWalkSoundInstance(stagBeetleEntity));
        } else if (packetEntity instanceof StickBugEntity stickBugEntity) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(new BugsWalkSoundInstance(stickBugEntity));
        } else if (packetEntity instanceof WeevilEntity weevilEntity) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(new BugsWalkSoundInstance(weevilEntity));
        } else if (packetEntity instanceof SnailEntity snailEntity) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(new SnailWalkSoundInstance(snailEntity));
        }
    }
}
