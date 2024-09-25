package com.github.eterdelta.crittersandcompanions.network;

import com.github.eterdelta.crittersandcompanions.platform.service.INetwork;
import com.github.eterdelta.crittersandcompanions.platform.Services;

public class CACPacketHandler {

    public static final INetwork.Sender<ClientboundBubbleStatePacket> BUBBLE_STATE =
            Services.NETWORK.createSender(ClientboundBubbleStatePacket.class, new ClientboundBubbleStatePacket.Handler());

    public static final INetwork.Sender<ClientboundGrapplingStatePacket> GRAPPLING_STATE =
            Services.NETWORK.createSender(ClientboundGrapplingStatePacket.class, new ClientboundGrapplingStatePacket.Handler());

    public static final INetwork.Sender<ClientboundSilkLeashStatePacket> SILK_LEASH_STATE =
            Services.NETWORK.createSender(ClientboundSilkLeashStatePacket.class, new ClientboundSilkLeashStatePacket.Handler());

    public static void registerPackets() {
        // Load this class
    }
}
