package io.github.bonsaistudi0s.crittersandcompanions.common.network;

import dev.architectury.networking.NetworkManager;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.ISilkLeashState;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ClientboundSilkLeashStatePacket(List<LeashData> leashDataList) implements ICACPacketPayload {

    public record LeashData(int leashOwner, IntList leashingEntities, IntList leashedByEntities) {
    }

    public static final ResourceLocation ID = CrittersAndCompanions.createId("silk_leash_state");

    public ClientboundSilkLeashStatePacket(LeashData... leashData) {
        this(Arrays.asList(leashData));
    }

    public ClientboundSilkLeashStatePacket(FriendlyByteBuf buf) {
        this(readList(buf));
    }

    private static List<LeashData> readList(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        var dataList = new ArrayList<LeashData>(size);

        for (int i = 0; i < size; i++) {
            int leashOwner = buf.readVarInt();
            IntList leashingEntities = buf.readIntIdList();
            IntList leashedByEntities = buf.readIntIdList();
            dataList.add(new LeashData(leashOwner, leashingEntities, leashedByEntities));
        }

        return dataList;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(leashDataList.size());
        for (LeashData data : leashDataList) {
            buf.writeVarInt(data.leashOwner);
            buf.writeIntIdList(data.leashingEntities);
            buf.writeIntIdList(data.leashedByEntities);
        }
    }

    @Override
    public ResourceLocation getId() {
        return ID;
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
