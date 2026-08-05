package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

import dev.architectury.networking.NetworkManager;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IBubbleState;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.ParticleUtils;

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

    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }

            var player = (Player) level.getEntity(playerId);

            if (player instanceof IBubbleState bubbleState) {
                bubbleState.setBubbleActive(state);
                if (state && octopusId().isPresent()) {
                    var octopus = level.getEntity(octopusId.getAsInt());
                    if (octopus != null) {
                        ParticleUtils.drawParticleLine(
                                ParticleTypes.BUBBLE,
                                level,
                                octopus.position(),
                                player.getEyePosition(),
                                0.2D,
                                Vec3.ZERO
                        );
                    }
                }
            }
        });
    }
}
