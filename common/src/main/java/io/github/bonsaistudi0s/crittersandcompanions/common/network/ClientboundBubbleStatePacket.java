package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import dev.architectury.networking.NetworkManager;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IBubbleState;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.ParticleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.OptionalInt;

public record ClientboundBubbleStatePacket(boolean state, int playerId, OptionalInt octopusId) implements ICACPacketPayload {

    public static final ResourceLocation ID = CrittersAndCompanions.createId("bubble_state");

    public ClientboundBubbleStatePacket(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readVarInt(), buf.readBoolean() ? OptionalInt.of(buf.readVarInt()) : OptionalInt.empty());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.state);
        buf.writeVarInt(this.playerId);
        buf.writeBoolean(this.octopusId.isPresent());
        this.octopusId.ifPresent(buf::writeVarInt);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
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
