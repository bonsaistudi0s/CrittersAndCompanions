package com.github.eterdelta.crittersandcompanions.network;

import com.github.eterdelta.crittersandcompanions.capability.CACCapabilities;
import com.github.eterdelta.crittersandcompanions.capability.IGrapplingStateCapability;
import com.github.eterdelta.crittersandcompanions.entity.GrapplingHookEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class ClientboundGrapplingStatePacket {
    private final Optional<Integer> hook;
    private final int player;

    public ClientboundGrapplingStatePacket(Optional<Integer> hook, int player) {
        this.hook = hook;
        this.player = player;
    }

    public ClientboundGrapplingStatePacket(FriendlyByteBuf byteBuf) {
        this(byteBuf.readOptional(FriendlyByteBuf::readInt), byteBuf.readInt());
    }

    public void write(FriendlyByteBuf byteBuf) {
        byteBuf.writeOptional(this.hook, FriendlyByteBuf::writeInt);
        byteBuf.writeInt(this.player);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Player player = (Player) Minecraft.getInstance().level.getEntity(this.player);

            if (player != null) {
                LazyOptional<IGrapplingStateCapability> capability = player.getCapability(CACCapabilities.GRAPPLING_STATE);
                GrapplingHookEntity grapplingHook = this.hook.map(id -> (GrapplingHookEntity) Minecraft.getInstance().level.getEntity(id)).orElse(null);
                capability.ifPresent(grappleState -> grappleState.setHook(grapplingHook));
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
