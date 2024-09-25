package com.github.eterdelta.crittersandcompanions.network;

import com.github.eterdelta.crittersandcompanions.entity.GrapplingHookEntity;
import com.github.eterdelta.crittersandcompanions.extension.IGrapplingState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public record ClientboundGrapplingStatePacket(Optional<Integer> hook, int playerId) {

    public static class Handler implements IPacketHandler<ClientboundGrapplingStatePacket> {

        @Override
        public ClientboundGrapplingStatePacket read(FriendlyByteBuf byteBuf) {
            return new ClientboundGrapplingStatePacket(byteBuf.readOptional(FriendlyByteBuf::readInt), byteBuf.readInt());
        }

        @Override
        public void write(ClientboundGrapplingStatePacket packet, FriendlyByteBuf byteBuf) {
            byteBuf.writeOptional(packet.hook, FriendlyByteBuf::writeInt);
            byteBuf.writeInt(packet.playerId);
        }

        @Override
        public void handle(ClientboundGrapplingStatePacket packet) {
            Player player = (Player) Minecraft.getInstance().level.getEntity(packet.playerId);

            if (player instanceof IGrapplingState grappleState) {
                GrapplingHookEntity grapplingHook = packet.hook.map(id -> (GrapplingHookEntity) Minecraft.getInstance().level.getEntity(id)).orElse(null);
                grappleState.setHook(grapplingHook);
            }
        }
    }

}
