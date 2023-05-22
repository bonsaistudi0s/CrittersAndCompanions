package com.github.eterdelta.crittersandcompanions.network;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class CACPacketHandler {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CrittersAndCompanions.MODID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    public static void registerPackets() {
        INSTANCE.registerMessage(0,
                ClientboundBubbleStatePacket.class,
                ClientboundBubbleStatePacket::write,
                ClientboundBubbleStatePacket::new,
                ClientboundBubbleStatePacket::handle);
    }
}
