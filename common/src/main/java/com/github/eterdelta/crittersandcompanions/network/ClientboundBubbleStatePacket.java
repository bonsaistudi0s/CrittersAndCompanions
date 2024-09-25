package com.github.eterdelta.crittersandcompanions.network;

import com.github.eterdelta.crittersandcompanions.extension.IBubbleState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record ClientboundBubbleStatePacket(boolean state, int playerId) {

    public static class Handler implements IPacketHandler<ClientboundBubbleStatePacket> {
        @Override
        public ClientboundBubbleStatePacket read(FriendlyByteBuf byteBuf) {
            return new ClientboundBubbleStatePacket(byteBuf.readBoolean(), byteBuf.readInt());
        }

        @Override
        public void write(ClientboundBubbleStatePacket packet, FriendlyByteBuf byteBuf) {
            byteBuf.writeBoolean(packet.state);
            byteBuf.writeInt(packet.playerId);
        }

        @Override
        public void handle(ClientboundBubbleStatePacket packet) {
            Player player = (Player) Minecraft.getInstance().level.getEntity(packet.playerId);

            if (player instanceof IBubbleState bubbleState) {
                bubbleState.setBubbleActive(packet.state);
            }
        }
    }

}
