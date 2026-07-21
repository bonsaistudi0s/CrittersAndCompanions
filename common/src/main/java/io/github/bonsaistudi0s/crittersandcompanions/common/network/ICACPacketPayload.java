package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface ICACPacketPayload {
    void encode(FriendlyByteBuf buf);
    ResourceLocation getId();
}
