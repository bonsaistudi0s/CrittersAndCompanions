package com.github.eterdelta.crittersandcompanions.platform;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.network.IPacketHandler;
import com.github.eterdelta.crittersandcompanions.platform.service.INetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Locale;

public class FabricNetwork implements INetwork {

    @Override
    public <T> Sender<T> createSender(Class<T> clazz, IPacketHandler<T> handler) {
        var id = new ResourceLocation(CrittersAndCompanions.MODID, clazz.getSimpleName().toLowerCase(Locale.ROOT));

        ClientPlayNetworking.registerGlobalReceiver(id, (client, handler1, buf, responseSender) -> {
            var packet = handler.read(buf);
            client.execute(() -> handler.handle(packet));
        });

        return new Sender<>() {
            private FriendlyByteBuf write(T packet) {
                var buf = PacketByteBufs.create();
                handler.write(packet, buf);
                return buf;
            }

            @Override
            public void sendToPlayer(ServerPlayer player, T packet) {
                ServerPlayNetworking.send(player, id, write(packet));
            }

            @Override
            public void sendToTracking(Entity entity, T packet) {
                if(entity instanceof ServerPlayer player) sendToPlayer(player, packet);
            }
        };
    }

}
