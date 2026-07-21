package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CACPacketHandler {

    public static void registerPackets() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ClientboundBubbleStatePacket.ID, (buf, context) ->
                new ClientboundBubbleStatePacket(buf).handle(context)
        );

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ClientboundGrapplingStatePacket.ID, (buf, context) ->
                new ClientboundGrapplingStatePacket(buf).handle(context)
        );

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ClientboundSilkLeashStatePacket.ID, (buf, context) ->
                new ClientboundSilkLeashStatePacket(buf).handle(context)
        );
    }

    public static <T extends ICACPacketPayload> void sendToPlayer(ServerPlayer player, T payload) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        payload.encode(buf);
        NetworkManager.sendToPlayer(player, payload.getId(), buf);
    }

    public static <T extends ICACPacketPayload> void sendToTracking(Entity entity, T payload) {
        sendToTracking(entity, payload, false);
    }

    public static <T extends ICACPacketPayload> void sendToTrackingAndSelf(Entity entity, T payload) {
        sendToTracking(entity, payload, true);
    }

    private static <T extends ICACPacketPayload> void sendToTracking(Entity entity, T payload, boolean includeSelf) {
        if (entity.level().isClientSide()) {
            throw new IllegalStateException("Cannot send clientbound payloads on the client");
        }

        if (!(entity.level().getChunkSource() instanceof ServerChunkCache chunkCache)) {
            return;
        }

        var trackedEntity = chunkCache.chunkMap.entityMap.get(entity.getId());
        if (trackedEntity == null) {
            return;
        }

        var players = trackedEntity.seenBy.stream()
                .map(ServerPlayerConnection::getPlayer)
                .collect(Collectors.toCollection(ArrayList::new));

        if (includeSelf && entity instanceof ServerPlayer player) {
            players.add(player);
        }

        var buf = new FriendlyByteBuf(Unpooled.buffer());
        payload.encode(buf);
        NetworkManager.sendToPlayers(players, payload.getId(), buf);
    }
}
