package com.github.eterdelta.crittersandcompanions.network;

import net.minecraft.network.FriendlyByteBuf;

public interface IPacketHandler<T> {

    T read(FriendlyByteBuf byteBuf);

    void write(T packet, FriendlyByteBuf byteBuf);

    void handle(T packet);

}
