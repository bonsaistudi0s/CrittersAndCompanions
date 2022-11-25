package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.client.sound.DragonflySoundInstance;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(at = @At(value = "JUMP", opcode = Opcodes.IFNULL, ordinal = 0, shift = At.Shift.AFTER), method = "handleAddEntity", locals = LocalCapture.CAPTURE_FAILHARD)
    private void handleAddMob(ClientboundAddEntityPacket clientboundAddEntityPacket, CallbackInfo ci, EntityType<?> entityType, Entity packetEntity) {
        if (packetEntity instanceof DragonflyEntity dragonflyEntity) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(new DragonflySoundInstance(dragonflyEntity));
        }
    }
}
