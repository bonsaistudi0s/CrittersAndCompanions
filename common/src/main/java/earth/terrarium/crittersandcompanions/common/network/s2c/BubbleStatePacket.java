package earth.terrarium.crittersandcompanions.common.network.s2c;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.capability.Bubbleable;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public record BubbleStatePacket(int player, boolean state) implements Packet<BubbleStatePacket> {
    public static Handler HANDLER = new Handler();

    @Override
    public PacketType<BubbleStatePacket> type() {
        return HANDLER;
    }

    public static class Handler implements ClientboundPacketType<BubbleStatePacket>, CodecPacketType<BubbleStatePacket> {
        public static final ResourceLocation ID = new ResourceLocation(CrittersAndCompanions.MODID, "bubble_state");
        public static ByteCodec<BubbleStatePacket> CODEC = ObjectByteCodec.create(
            ByteCodec.INT.fieldOf(BubbleStatePacket::player),
            ByteCodec.BOOLEAN.fieldOf(BubbleStatePacket::state),
            BubbleStatePacket::new
        );

        @Override
        public Runnable handle(BubbleStatePacket message) {
            return () -> {
               Entity entity = Minecraft.getInstance().level.getEntity(message.player());
                if (entity instanceof Bubbleable bubbleState) {
                    bubbleState.crittersandcompanions$setActive(message.state());
                }
            };
        }

        @Override
        public ByteCodec<BubbleStatePacket> codec() {
            return CODEC;
        }

        @Override
        public Class<BubbleStatePacket> type() {
            return BubbleStatePacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
