package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import dev.architectury.networking.NetworkManager;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IBubbleState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record ClientboundBubbleStatePacket(boolean state, int playerId) implements ICACPacketPayload {

    public static final ResourceLocation ID = CrittersAndCompanions.createId("bubble_state");

    public ClientboundBubbleStatePacket(FriendlyByteBuf buf) {
        this(buf.readBoolean(), buf.readVarInt());
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.state);
        buf.writeVarInt(this.playerId);
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
            }
        });
    }
}
