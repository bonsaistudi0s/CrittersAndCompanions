package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

import dev.architectury.networking.NetworkManager;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;

public record ClientboundBubbleStatePacket(boolean state, int playerId,
                                           OptionalInt octopusId) implements CustomPacketPayload {

    public static final CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, ClientboundBubbleStatePacket> TYPE = new TypeAndCodec<>(
            new Type<>(CrittersAndCompanions.createId("bubble_state")),
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,
                    ClientboundBubbleStatePacket::state,
                    ByteBufCodecs.VAR_INT,
                    ClientboundBubbleStatePacket::playerId,
                    CACCodecs.OPTIONAL_VAR_INT,
                    ClientboundBubbleStatePacket::octopusId,
                    ClientboundBubbleStatePacket::new
            )
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }

}
