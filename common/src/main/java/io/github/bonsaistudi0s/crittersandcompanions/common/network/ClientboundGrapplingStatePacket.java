package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import dev.architectury.networking.NetworkManager;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.GrapplingHookEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IGrapplingState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.OptionalInt;

public record ClientboundGrapplingStatePacket(OptionalInt hook, int playerId) implements ICACPacketPayload {

    public static final ResourceLocation ID = CrittersAndCompanions.createId("grappling_state");

    public ClientboundGrapplingStatePacket(FriendlyByteBuf buf) {
        this(
                buf.readBoolean() ? OptionalInt.of(buf.readVarInt()) : OptionalInt.empty(),
                buf.readVarInt()
        );
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.hook.isPresent());
        this.hook.ifPresent(buf::writeVarInt);

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
