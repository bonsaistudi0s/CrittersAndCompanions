package com.github.eterdelta.crittersandcompanions.platform;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.network.IPacketHandler;
import com.github.eterdelta.crittersandcompanions.platform.service.INetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ForgeNetwork implements INetwork {

    private final String protocol = "2";

    private final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CrittersAndCompanions.MODID, "main"),
            () -> protocol,
            protocol::equals,
            protocol::equals
    );

    private int messageId = 0;

    @Override
    public <T> Sender<T> createSender(Class<T> clazz, IPacketHandler<T> handler) {
        channel.registerMessage(
                messageId++, clazz,
                handler::write,
                handler::read,
                (packet, contextSupplier) -> {
                    var context = contextSupplier.get();

                    context.enqueueWork(() -> handler.handle(packet));

                    context.setPacketHandled(true);
                }
        );

        return new Sender<>() {
            @Override
            public void sendToPlayer(ServerPlayer player, T packet) {
                channel.send(PacketDistributor.PLAYER.with(() -> player), packet);
            }

            @Override
            public void sendToTracking(Entity entity, T packet) {
                if (entity.level().isClientSide()) return;
                channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), packet);
            }
        };
    }

}
