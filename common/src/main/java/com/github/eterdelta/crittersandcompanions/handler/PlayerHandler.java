package com.github.eterdelta.crittersandcompanions.handler;

import com.github.eterdelta.crittersandcompanions.entity.DumboOctopusEntity;
import com.github.eterdelta.crittersandcompanions.entity.KoiFishEntity;
import com.github.eterdelta.crittersandcompanions.extension.IBubbleState;
import com.github.eterdelta.crittersandcompanions.extension.IGrapplingState;
import com.github.eterdelta.crittersandcompanions.extension.ISilkLeashState;
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
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PlayerHandler {

    public static InteractionHand getOppositeHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    @Nullable
    public static InteractionResult onPlayerEntityInteract(Entity target, UseOnContext context) {
        var isClient = target.level().isClientSide();
        if (!(context.getPlayer() instanceof ISilkLeashState playerLeashState)) return null;
        if (!(target instanceof LivingEntity entity)) return null;

        ItemStack handStack = context.getItemInHand();
        ItemStack otherHandStack = context.getPlayer().getItemInHand(getOppositeHand(context.getHand()));

        Set<LivingEntity> playerLeashingEntities = playerLeashState.getLeashingEntities();

        if (otherHandStack.is(CACItems.SILK_LEAD.get())) return null;

        if ((playerLeashingEntities.isEmpty() || playerLeashingEntities.contains(entity))
                && !(handStack.is(CACItems.SILK_LEAD.get()) || handStack.is(Items.LEAD))
                && context.getHand() == InteractionHand.MAIN_HAND) {

            if(isClient) return InteractionResult.SUCCESS;

            int unleashedStates = 0;
            unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates(entity, null) - 1);
            unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates(null, entity) - 1);
            if (unleashedStates > 0) {
                ItemEntity leadEntity = new ItemEntity(context.getLevel(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(CACItems.SILK_LEAD.get(), unleashedStates));
                context.getLevel().addFreshEntity(leadEntity);

                playerLeashState.sendLeashState();
                if (entity instanceof ISilkLeashState entityLeashState) {
                    entityLeashState.sendLeashState();
                }

                return InteractionResult.CONSUME;
            }
        } else {
            LivingEntity uniqueLeash = Iterables.getFirst(playerLeashingEntities, null);

            if (uniqueLeash != null && SilkLeashItem.updateLeashStates(uniqueLeash, entity) != 0) {
                SilkLeashItem.updateLeashStates(context.getPlayer(), null);
                return InteractionResult.sidedSuccess(isClient);
            }
        }

        return null;
    }

    public static void onPlayerTick(Player player) {
        if (player.level().isClientSide()) return;

        List<KoiFishEntity> nearKoiFishes = player.level().getEntitiesOfClass(KoiFishEntity.class, player.getBoundingBox().inflate(10.0D), EntitySelector.ENTITY_STILL_ALIVE);

        if (nearKoiFishes.size() >= 3) {
            player.addEffect(new MobEffectInstance(MobEffects.LUCK, 210, 0, false, false));
        }
    }

    public static void onPlayerStartTracking(Entity target, Entity from) {
        if (target instanceof LivingEntity trackedEntity && from instanceof ServerPlayer player && target instanceof ISilkLeashState trackedState) {
            CACPacketHandler.SILK_LEASH_STATE.sendToPlayer(player,
                    new ClientboundSilkLeashStatePacket(
                            new ClientboundSilkLeashStatePacket.LeashData(
                                    trackedEntity.getId(),
                                    new IntArrayList(trackedState.getLeashingEntities().stream().mapToInt(Entity::getId).toArray()),
                                    new IntArrayList(trackedState.getLeashedByEntities().stream().mapToInt(Entity::getId).toArray())
                            )
                    ));
        }

        if (target instanceof Player trackedPlayer && from instanceof ServerPlayer fromPlayer) {
            var bubbleState = (IBubbleState) trackedPlayer;
            var grappleState = (IGrapplingState) trackedPlayer;

            CACPacketHandler.BUBBLE_STATE.sendToPlayer(fromPlayer, new ClientboundBubbleStatePacket(bubbleState.isBubbleActive(), trackedPlayer.getId()));
            CACPacketHandler.GRAPPLING_STATE.sendToPlayer(fromPlayer, new ClientboundGrapplingStatePacket(grappleState.getHook() != null ? Optional.of(grappleState.getHook().getId()) : Optional.empty(), trackedPlayer.getId()));
        }
    }

    public static void onPlayerStopTracking(Entity target, Entity by) {
        if (target instanceof DumboOctopusEntity dumboOctopus) {
            if (dumboOctopus.getBubbledPlayer() == by) {
                dumboOctopus.sendBubble((ServerPlayer) by, false);
            }
        }
    }
}
