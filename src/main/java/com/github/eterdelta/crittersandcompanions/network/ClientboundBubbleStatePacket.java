package com.github.eterdelta.crittersandcompanions.network;

import com.github.eterdelta.crittersandcompanions.capability.CACCapabilities;
import com.github.eterdelta.crittersandcompanions.capability.IBubbleStateCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundBubbleStatePacket {
    private final boolean state;
    private final int player;

    public ClientboundBubbleStatePacket(boolean state, int player) {
        this.state = state;
        this.player = player;
    }

    public ClientboundBubbleStatePacket(FriendlyByteBuf byteBuf) {
        this(byteBuf.readBoolean(), byteBuf.readInt());
    }

    public void write(FriendlyByteBuf byteBuf) {
        byteBuf.writeBoolean(this.state);
        byteBuf.writeInt(this.player);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Player player = (Player) Minecraft.getInstance().level.getEntity(this.player);

            if (player != null) {
                LazyOptional<IBubbleStateCapability> capability = player.getCapability(CACCapabilities.BUBBLE_STATE);
                capability.ifPresent(bubbleState -> bubbleState.setActive(this.state));
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
