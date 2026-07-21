package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.architectury.networking.NetworkManager;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.ISilkLeashState;
import it.unimi.dsi.fastutil.ints.IntList;

public record ClientboundSilkLeashStatePacket(List<LeashData> leashDataList) implements CustomPacketPayload {

    public record LeashData(int leashOwner, IntList leashingEntities, IntList leashedByEntities) {
    }

    public static final TypeAndCodec<FriendlyByteBuf, ClientboundSilkLeashStatePacket> TYPE = new TypeAndCodec<>(
            new Type<>(CrittersAndCompanions.createId("silk_leash_state")),
            StreamCodec.of(ClientboundSilkLeashStatePacket::write, ClientboundSilkLeashStatePacket::read)
    );

    public ClientboundSilkLeashStatePacket(LeashData... leashData) {
        this(Arrays.asList(leashData));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }

    private static ClientboundSilkLeashStatePacket read(FriendlyByteBuf byteBuf) {
        int size = byteBuf.readVarInt();
        var leashDataList = new ArrayList<LeashData>(size);

        for (int i = 0; i < size; i++) {
            int leashOwner = byteBuf.readVarInt();
            IntList leashingEntities = byteBuf.readIntIdList();
            IntList leashedByEntities = byteBuf.readIntIdList();
            leashDataList.add(new LeashData(leashOwner, leashingEntities, leashedByEntities));
        }

        return new ClientboundSilkLeashStatePacket(leashDataList);
    }

    private static void write(FriendlyByteBuf byteBuf, ClientboundSilkLeashStatePacket packet) {
        byteBuf.writeVarInt(packet.leashDataList.size());
        for (LeashData data : packet.leashDataList) {
            byteBuf.writeVarInt(data.leashOwner);
            byteBuf.writeIntIdList(data.leashingEntities);
            byteBuf.writeIntIdList(data.leashedByEntities);
        }
    }

    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }

            for (var data : leashDataList) {
                var entity = level.getEntity(data.leashOwner());

                if (entity instanceof ISilkLeashState leashState) {
                    leashState.getLeashingEntities().clear();
                    leashState.getLeashedByEntities().clear();

                    data.leashingEntities().forEach(id -> {
                        var leashingEntity = level.getEntity(id);
                        if (leashingEntity instanceof LivingEntity) {
                            leashState.getLeashingEntities().add((LivingEntity) leashingEntity);
                        }
                    });
                    data.leashedByEntities().forEach(id -> {
                        var leashedByEntity = level.getEntity(id);
                        if (leashedByEntity instanceof LivingEntity) {
                            leashState.getLeashedByEntities().add(((LivingEntity) leashedByEntity));
                        }
                    });
                }
            }
        });
    }
}
