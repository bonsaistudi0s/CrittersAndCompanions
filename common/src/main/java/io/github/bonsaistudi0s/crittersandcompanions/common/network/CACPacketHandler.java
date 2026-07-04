package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.stream.Collectors;

import dev.architectury.networking.NetworkManager;

public class CACPacketHandler {

    public static void registerPackets() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ClientboundBubbleStatePacket.TYPE.type(), ClientboundBubbleStatePacket.TYPE.codec(), ClientboundBubbleStatePacket::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ClientboundGrapplingStatePacket.TYPE.type(), ClientboundGrapplingStatePacket.TYPE.codec(), ClientboundGrapplingStatePacket::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ClientboundSilkLeashStatePacket.TYPE.type(), ClientboundSilkLeashStatePacket.TYPE.codec(), ClientboundSilkLeashStatePacket::handle);
    }

    public static <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T payload) {
        NetworkManager.sendToPlayer(player, payload);
    }

    public static <T extends CustomPacketPayload> void sendToTracking(Entity entity, T payload) {
        sendToTracking(entity, payload, false);
    }

    public static <T extends CustomPacketPayload> void sendToTrackingAndSelf(Entity entity, T payload) {
        sendToTracking(entity, payload, true);
    }

    private static <T extends CustomPacketPayload> void sendToTracking(Entity entity, T payload, boolean includeSelf) {
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

        NetworkManager.sendToPlayers(players, payload);
    }
}
