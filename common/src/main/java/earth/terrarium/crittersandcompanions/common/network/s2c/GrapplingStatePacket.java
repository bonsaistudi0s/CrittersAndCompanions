package earth.terrarium.crittersandcompanions.common.network.s2c;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.capability.Grapplable;
import earth.terrarium.crittersandcompanions.common.entity.GrapplingHookEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public record GrapplingStatePacket(int player, Optional<Integer> hook) implements Packet<GrapplingStatePacket> {
    public static Handler HANDLER = new Handler();

    @Override
    public PacketType<GrapplingStatePacket> type() {
        return HANDLER;
    }

    public static class Handler implements ClientboundPacketType<GrapplingStatePacket>, CodecPacketType<GrapplingStatePacket> {
        public static final ResourceLocation ID = new ResourceLocation(CrittersAndCompanions.MODID, "grappling_state");
        public static ByteCodec<GrapplingStatePacket> CODEC = ObjectByteCodec.create(
            ByteCodec.INT.fieldOf(GrapplingStatePacket::player),
            ByteCodec.INT.optionalFieldOf(GrapplingStatePacket::hook),
            GrapplingStatePacket::new
        );

        @Override
        public Runnable handle(GrapplingStatePacket message) {
            return () -> {
                Entity entity = Minecraft.getInstance().level.getEntity(message.player());

                if (entity instanceof Player player && entity instanceof Grapplable grappleable) {
                    GrapplingHookEntity grapplingHook = message.hook().map(id -> (GrapplingHookEntity) Minecraft.getInstance().level.getEntity(id)).orElse(null);
                    grappleable.setHook(grapplingHook);
                }
            };
        }

        @Override
        public ByteCodec<GrapplingStatePacket> codec() {
            return CODEC;
        }

        @Override
        public Class<GrapplingStatePacket> type() {
            return GrapplingStatePacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
