package earth.terrarium.crittersandcompanions.common.network.s2c;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.capability.SilkLeashable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public record SilkLeashStatePacket(List<LeashData> states) implements Packet<SilkLeashStatePacket> {
    public static Handler HANDLER = new Handler();

    public SilkLeashStatePacket(LeashData ... states) {
        this(List.of(states));
    }

    @Override
    public PacketType<SilkLeashStatePacket> type() {
        return HANDLER;
    }

    public static class Handler implements ClientboundPacketType<SilkLeashStatePacket>, CodecPacketType<SilkLeashStatePacket> {
        public static final ResourceLocation ID = new ResourceLocation(CrittersAndCompanions.MODID, "silk_leash_state");
        public static ByteCodec<SilkLeashStatePacket> CODEC = ObjectByteCodec.create(
            LeashData.CODEC.listOf().fieldOf(SilkLeashStatePacket::states),
            SilkLeashStatePacket::new
        );

        @Override
        public Runnable handle(SilkLeashStatePacket message) {
            return () -> {
                ClientLevel level = Minecraft.getInstance().level;

                for (LeashData data : message.states) {
                    Entity entity = level.getEntity(data.entityId());

                    if (entity instanceof SilkLeashable leashable) {
                        leashable.getLeashingEntities().clear();
                        leashable.getLeashedByEntities().clear();

                        data.leashingEntities().forEach(id -> {
                            Entity leashingEntity = level.getEntity(id);
                            if (leashingEntity instanceof LivingEntity) {
                                leashable.getLeashingEntities().add((LivingEntity) leashingEntity);
                            }
                        });
                        data.leashedByEntities().forEach(id -> {
                            Entity leashedByEntity = level.getEntity(id);
                            if (leashedByEntity instanceof LivingEntity) {
                                leashable.getLeashedByEntities().add(((LivingEntity) leashedByEntity));
                            }
                        });
                    }
                }
            };
        }

        @Override
        public ByteCodec<SilkLeashStatePacket> codec() {
            return CODEC;
        }

        @Override
        public Class<SilkLeashStatePacket> type() {
            return SilkLeashStatePacket.class;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }

    public record LeashData(int entityId, List<Integer> leashingEntities, List<Integer> leashedByEntities) {
        public static ByteCodec<LeashData> CODEC = ObjectByteCodec.create(
            ByteCodec.INT.fieldOf(LeashData::entityId),
            ByteCodec.INT.listOf().fieldOf(LeashData::leashingEntities),
            ByteCodec.INT.listOf().fieldOf(LeashData::leashedByEntities),
            LeashData::new
        );
    }
}
