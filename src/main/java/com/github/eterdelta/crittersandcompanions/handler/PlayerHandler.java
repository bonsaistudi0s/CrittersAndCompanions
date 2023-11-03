package com.github.eterdelta.crittersandcompanions.handler;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.capability.CACCapabilities;
import com.github.eterdelta.crittersandcompanions.capability.IBubbleStateCapability;
import com.github.eterdelta.crittersandcompanions.capability.IGrapplingStateCapability;
import com.github.eterdelta.crittersandcompanions.capability.ISilkLeashStateCapability;
import com.github.eterdelta.crittersandcompanions.entity.DumboOctopusEntity;
import com.github.eterdelta.crittersandcompanions.entity.ILeashStateEntity;
import com.github.eterdelta.crittersandcompanions.entity.KoiFishEntity;
import com.github.eterdelta.crittersandcompanions.item.SilkLeashItem;
import com.github.eterdelta.crittersandcompanions.network.CACPacketHandler;
import com.github.eterdelta.crittersandcompanions.network.ClientboundBubbleStatePacket;
import com.github.eterdelta.crittersandcompanions.network.ClientboundGrapplingStatePacket;
import com.github.eterdelta.crittersandcompanions.network.ClientboundSilkLeashStatePacket;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID)
public class PlayerHandler {

    public static InteractionHand getOppositeHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    @SubscribeEvent
    public static void onPlayerEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getSide().isServer() && event.getTarget() instanceof LivingEntity entity) {
            Player player = event.getEntity();
            ItemStack handStack = player.getItemInHand(event.getHand());
            ItemStack otherHandStack = player.getItemInHand(getOppositeHand(event.getHand()));

            LazyOptional<ISilkLeashStateCapability> playerLeashCap = player.getCapability(CACCapabilities.SILK_LEASH_STATE);

            playerLeashCap.ifPresent(playerLeashState -> {
                Set<LivingEntity> playerLeashingEntities = playerLeashState.getLeashingEntities();

                if (!otherHandStack.is(CACItems.SILK_LEAD.get())) {
                    if ((playerLeashingEntities.isEmpty() || playerLeashingEntities.contains(entity))
                            && !(handStack.is(CACItems.SILK_LEAD.get()) || handStack.is(Items.LEAD))
                            && event.getHand() == InteractionHand.MAIN_HAND) {
                        int unleashedStates = 0;
                        unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates(entity, null) - 1);
                        unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates(null, entity) - 1);
                        if (unleashedStates > 0) {
                            ItemEntity leadEntity = new ItemEntity(event.getLevel(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(CACItems.SILK_LEAD.get(), unleashedStates));
                            event.getLevel().addFreshEntity(leadEntity);
                            event.setCanceled(true);
                            event.setCancellationResult(InteractionResult.SUCCESS);
                        }
                    } else {
                        LivingEntity uniqueLeash = Iterables.getFirst(playerLeashingEntities, null);
                        if (uniqueLeash != null) {
                            if (SilkLeashItem.updateLeashStates(uniqueLeash, entity) != 0) {
                                SilkLeashItem.updateLeashStates(player, null);
                                event.setCanceled(true);
                                event.setCancellationResult(InteractionResult.SUCCESS);
                            }
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.side.isServer()) {
                List<KoiFishEntity> nearKoiFishes = event.player.getLevel().getEntitiesOfClass(KoiFishEntity.class, event.player.getBoundingBox().inflate(10.0D), EntitySelector.ENTITY_STILL_ALIVE);

                if (nearKoiFishes.size() >= 3) {
                    event.player.addEffect(new MobEffectInstance(MobEffects.LUCK, 210, 0, false, false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity trackedEntity) {
            LazyOptional<ISilkLeashStateCapability> silkLeashCap = ((ILeashStateEntity) trackedEntity).getLeashStateCache();

            silkLeashCap.ifPresent(trackedState -> {
                CACPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new ClientboundSilkLeashStatePacket(
                                new ClientboundSilkLeashStatePacket.LeashData(
                                        trackedEntity.getId(),
                                        new IntArrayList(trackedState.getLeashingEntities().stream().mapToInt(Entity::getId).toArray()),
                                        new IntArrayList(trackedState.getLeashedByEntities().stream().mapToInt(Entity::getId).toArray())
                                )
                        ));
            });
        }
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
}
