package com.github.eterdelta.crittersandcompanions.network;

import com.github.eterdelta.crittersandcompanions.capability.CACCapabilities;
import com.github.eterdelta.crittersandcompanions.capability.ISilkLeashStateCapability;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ClientboundSilkLeashStatePacket {
    private final List<LeashData> leashDataList;

    public ClientboundSilkLeashStatePacket(List<LeashData> leashDataList) {
        this.leashDataList = leashDataList;
    }

    public ClientboundSilkLeashStatePacket(LeashData... leashData) {
        this(Arrays.asList(leashData));
    }

    public ClientboundSilkLeashStatePacket(FriendlyByteBuf byteBuf) {
        int size = byteBuf.readInt();
        this.leashDataList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            int leashOwner = byteBuf.readInt();
            IntList leashingEntities = byteBuf.readIntIdList();
            IntList leashedByEntities = byteBuf.readIntIdList();
            this.leashDataList.add(new LeashData(leashOwner, leashingEntities, leashedByEntities));
        }
    }

    public void write(FriendlyByteBuf byteBuf) {
        byteBuf.writeInt(this.leashDataList.size());
        for (LeashData data : this.leashDataList) {
            byteBuf.writeInt(data.leashOwner);
            byteBuf.writeIntIdList(data.leashingEntities);
            byteBuf.writeIntIdList(data.leashedByEntities);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;

            for (LeashData data : leashDataList) {
                Entity entity = level.getEntity(data.leashOwner());

                if (entity != null) {
                    LazyOptional<ISilkLeashStateCapability> capability = entity.getCapability(CACCapabilities.SILK_LEASH_STATE);
                    capability.ifPresent(leashState -> {
                        leashState.getLeashingEntities().clear();
                        leashState.getLeashedByEntities().clear();

                        data.leashingEntities().forEach(id -> {
                            Entity leashingEntity = level.getEntity(id);
                            if (leashingEntity instanceof LivingEntity) {
                                leashState.getLeashingEntities().add((LivingEntity) leashingEntity);
                            }
                        });
                        data.leashedByEntities().forEach(id -> {
                            Entity leashedByEntity = level.getEntity(id);
                            if (leashedByEntity instanceof LivingEntity) {
                                leashState.getLeashedByEntities().add(((LivingEntity) leashedByEntity));
                            }
                        });
                    });
                }
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }

    public record LeashData(int leashOwner, IntList leashingEntities, IntList leashedByEntities) {
    }
}
