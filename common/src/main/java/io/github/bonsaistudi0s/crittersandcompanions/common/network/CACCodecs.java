package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.OptionalInt;

public abstract class CACCodecs {

    public static final StreamCodec<FriendlyByteBuf, OptionalInt> OPTIONAL_VAR_INT = StreamCodec.of(
            (buffer, value) -> {
                buffer.writeBoolean(value.isPresent());
                value.ifPresent(buffer::writeVarInt);
            },
            buffer -> {
                if (buffer.readBoolean()) return OptionalInt.of(buffer.readVarInt());
                return OptionalInt.empty();
            }
    );
}
