package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import java.util.OptionalInt;

import dev.architectury.networking.NetworkManager;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.GrapplingHookEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IGrapplingState;

public record ClientboundGrapplingStatePacket(OptionalInt hook, int playerId) implements CustomPacketPayload {

    private static final StreamCodec<FriendlyByteBuf, OptionalInt> OPTIONAL_VAR_INT = StreamCodec.of(
            (buffer, value) -> {
                buffer.writeBoolean(value.isPresent());
                value.ifPresent(buffer::writeVarInt);
            },
            buffer -> {
                if (buffer.readBoolean()) return OptionalInt.of(buffer.readVarInt());
                return OptionalInt.empty();
            }
    );

    public static final TypeAndCodec<FriendlyByteBuf, ClientboundGrapplingStatePacket> TYPE = new TypeAndCodec<>(
            new Type<>(CrittersAndCompanions.createId("grappling_state")),
            StreamCodec.composite(
                    OPTIONAL_VAR_INT,
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

    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }

            var player = (Player) level.getEntity(playerId);

            if (!(player instanceof IGrapplingState grappleState)) {
                return;
            }

            hook.ifPresentOrElse(id -> {
                var entity = (GrapplingHookEntity) level.getEntity(id);
                grappleState.setHook(entity);
            }, () -> grappleState.setHook(null));
        });
    }

}
