package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.OptionalInt;

import dev.architectury.networking.NetworkManager;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;

public record ClientboundGrapplingStatePacket(OptionalInt hook, int playerId) implements CustomPacketPayload {

    public static final TypeAndCodec<FriendlyByteBuf, ClientboundGrapplingStatePacket> TYPE = new TypeAndCodec<>(
            new Type<>(CrittersAndCompanions.createId("grappling_state")),
            StreamCodec.composite(
                    CACCodecs.OPTIONAL_VAR_INT,
                    ClientboundGrapplingStatePacket::hook,
                    ByteBufCodecs.VAR_INT,
                    ClientboundGrapplingStatePacket::playerId,
                    ClientboundGrapplingStatePacket::new
            )
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }

}
