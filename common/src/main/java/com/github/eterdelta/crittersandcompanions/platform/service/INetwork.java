package com.github.eterdelta.crittersandcompanions.platform.service;

import com.github.eterdelta.crittersandcompanions.network.IPacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface INetwork {

    interface Sender<T> {
        void sendToPlayer(ServerPlayer player, T packet);

        void sendToTracking(Entity entity, T packet);
    }

    <T> Sender<T> createSender(Class<T> clazz, IPacketHandler<T> handler);

}
