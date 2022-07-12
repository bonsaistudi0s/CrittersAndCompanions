package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.client.sound.DragonflySoundInstance;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(at = @At(value = "JUMP", opcode = Opcodes.IFEQ, ordinal = 0), method = "handleAddMob(Lnet/minecraft/network/protocol/game/ClientboundAddMobPacket;)V", locals = LocalCapture.CAPTURE_FAILSOFT)
    private void handleAddMob(ClientboundAddMobPacket packet, CallbackInfo callback, LivingEntity packetEntity) {
        if (packetEntity instanceof DragonflyEntity dragonflyEntity) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(new DragonflySoundInstance(dragonflyEntity));
        }
    }
}
