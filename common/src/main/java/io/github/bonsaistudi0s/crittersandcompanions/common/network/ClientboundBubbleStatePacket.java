package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.NotNull;

import dev.architectury.networking.NetworkManager;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IBubbleState;

public record ClientboundBubbleStatePacket(boolean state, int playerId) implements CustomPacketPayload {

    public static final CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, ClientboundBubbleStatePacket> TYPE = new TypeAndCodec<>(
            new Type<>(CrittersAndCompanions.createId("bubble_state")),
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    ClientboundBubbleStatePacket::state,
                    ByteBufCodecs.VAR_INT,
                    ClientboundBubbleStatePacket::playerId,
                    ClientboundBubbleStatePacket::new
            )
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }

    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }

            var player = (Player) level.getEntity(playerId);

            if (player instanceof IBubbleState bubbleState) {
                bubbleState.setBubbleActive(state);
            }
        });
    }
}
