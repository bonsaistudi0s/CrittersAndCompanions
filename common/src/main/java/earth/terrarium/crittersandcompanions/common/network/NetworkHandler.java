package earth.terrarium.crittersandcompanions.common.network;

import com.teamresourceful.resourcefullib.common.network.NetworkChannel;
import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.network.s2c.SilkLeashStatePacket;
import earth.terrarium.crittersandcompanions.common.network.s2c.BubbleStatePacket;
import earth.terrarium.crittersandcompanions.common.network.s2c.GrapplingStatePacket;

@SuppressWarnings("UnstableApiUsage")
public class NetworkHandler {
    public static final NetworkChannel CHANNEL = new NetworkChannel(CrittersAndCompanions.MODID, 0, "main");

    public static void registerPackets() {
        CHANNEL.register(BubbleStatePacket.HANDLER);
        CHANNEL.register(GrapplingStatePacket.HANDLER);
        CHANNEL.register(SilkLeashStatePacket.HANDLER);
    }
}
