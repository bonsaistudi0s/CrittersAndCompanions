package io.github.bonsaistudi0s.crittersandcompanions.client.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import dev.architectury.networking.NetworkManager;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.GrapplingHookEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IBubbleState;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IGrapplingState;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.ISilkLeashState;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.ClientboundBubbleStatePacket;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.ClientboundGrapplingStatePacket;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.ClientboundSilkLeashStatePacket;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.ParticleUtils;

public class ClientPayloadHandler {

    public static void handleBubbleState(ClientboundBubbleStatePacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }

            var player = (Player) level.getEntity(packet.playerId());

            if (player instanceof IBubbleState bubbleState) {
                bubbleState.setBubbleActive(packet.state());
                if (packet.state() && packet.octopusId().isPresent()) {
                    var octopus = level.getEntity(packet.octopusId().getAsInt());
                    if (octopus != null) {
                        ParticleUtils.drawParticleLine(
                                ParticleTypes.BUBBLE,
                                level,
                                octopus.position(),
                                player.getEyePosition(),
                                0.2D,
                                Vec3.ZERO
                        );
                    }
                }
            }
        });
    }

    public static void handleGrapplingState(ClientboundGrapplingStatePacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }

            var player = (Player) level.getEntity(packet.playerId());

            if (!(player instanceof IGrapplingState grappleState)) {
                return;
            }

            packet.hook().ifPresentOrElse(
                    id -> {
                        var entity = (GrapplingHookEntity) level.getEntity(id);
                        grappleState.setHook(entity);
                    }, () -> grappleState.setHook(null)
            );
        });
    }

    public static void handleSilkLeashState(ClientboundSilkLeashStatePacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }

            for (var data : packet.leashDataList()) {
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
