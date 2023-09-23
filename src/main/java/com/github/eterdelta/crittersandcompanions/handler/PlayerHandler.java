package com.github.eterdelta.crittersandcompanions.handler;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.capability.*;
import com.github.eterdelta.crittersandcompanions.entity.DumboOctopusEntity;
import com.github.eterdelta.crittersandcompanions.entity.KoiFishEntity;
import com.github.eterdelta.crittersandcompanions.network.CACPacketHandler;
import com.github.eterdelta.crittersandcompanions.network.ClientboundBubbleStatePacket;
import com.github.eterdelta.crittersandcompanions.network.ClientboundGrapplingStatePacket;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID)
public class PlayerHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.side.isServer()) {
                List<KoiFishEntity> nearKoiFishes = event.player.getLevel().getEntitiesOfClass(KoiFishEntity.class, event.player.getBoundingBox().inflate(10.0D), EntitySelector.ENTITY_STILL_ALIVE);

                if (nearKoiFishes.size() >= 3) {
                    event.player.addEffect(new MobEffectInstance(MobEffects.LUCK, 210));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player trackedPlayer) {
            LazyOptional<IBubbleStateCapability> bubbleCap = trackedPlayer.getCapability(CACCapabilities.BUBBLE_STATE);

            bubbleCap.ifPresent(trackedState -> {
                CACPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new ClientboundBubbleStatePacket(trackedState.isActive(), trackedPlayer.getId()));
            });

            LazyOptional<IGrapplingStateCapability> grappleCap = trackedPlayer.getCapability(CACCapabilities.GRAPPLING_STATE);

            grappleCap.ifPresent(trackedState -> {
                CACPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new ClientboundGrapplingStatePacket(trackedState.getHook() != null ? Optional.of(trackedState.getHook().getId()) : Optional.empty(), trackedPlayer.getId()));
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerStopTracking(PlayerEvent.StopTracking event) {
        if (event.getTarget() instanceof DumboOctopusEntity dumboOctopus) {
            if (dumboOctopus.getBubbledPlayer() == event.getEntity()) {
                dumboOctopus.sendBubble((ServerPlayer) event.getEntity(), false);
            }
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            LazyOptional<IBubbleStateCapability> bubbleState = LazyOptional.of(BubbleState::new);
            ICapabilityProvider bubbleStateProvider = new ICapabilityProvider() {
                @Override
                public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
                    if (capability == CACCapabilities.BUBBLE_STATE) {
                        return bubbleState.cast();
                    }
                    return LazyOptional.empty();
                }
            };
            event.addCapability(new ResourceLocation(CrittersAndCompanions.MODID, "bubble_state"), bubbleStateProvider);

            LazyOptional<IGrapplingStateCapability> grapplingState = LazyOptional.of(GrapplingState::new);
            ICapabilityProvider grapplingStateProvider = new ICapabilityProvider() {
                @Override
                public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
                    if (capability == CACCapabilities.GRAPPLING_STATE) {
                        return grapplingState.cast();
                    }
                    return LazyOptional.empty();
                }
            };
            event.addCapability(new ResourceLocation(CrittersAndCompanions.MODID, "grappling_state"), grapplingStateProvider);
        }
    }
}
