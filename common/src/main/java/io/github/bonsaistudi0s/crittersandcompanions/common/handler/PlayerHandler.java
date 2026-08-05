package io.github.bonsaistudi0s.crittersandcompanions.common.handler;

import com.google.common.collect.Iterables;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.DumboOctopusEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.KoiFishEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IBubbleState;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IGrapplingState;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.ISilkLeashState;
import io.github.bonsaistudi0s.crittersandcompanions.common.item.SilkLeashItem;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.CACPacketHandler;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.ClientboundBubbleStatePacket;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.ClientboundGrapplingStatePacket;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.ClientboundSilkLeashStatePacket;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import io.github.bonsaistudi0s.crittersandcompanions.common.util.ParticleUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;
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

                return InteractionResult.sidedSuccess(isClient);
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
        if (player.level().isClientSide()) {
            if (player.tickCount % 2 == 0 && player instanceof IBubbleState bubbleState && bubbleState.isBubbleActive()) {
                ParticleUtils.drawPotionEffectLikeParticles(
                        ParticleTypes.BUBBLE,
                        player.level(),
                        AABB.ofSize(player.getEyePosition(), 1.0D, 1.0D, 1.0D),
                        Vec3.ZERO,
                        1
                );
            }
        } else {
            var nearKoiFishes = player.level().getEntitiesOfClass(KoiFishEntity.class, player.getBoundingBox().inflate(10.0D), EntitySelector.ENTITY_STILL_ALIVE);
            if (nearKoiFishes.size() >= 3) {
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 210, 0, false, false));
            }
        }
    }

    public static void onPlayerStartTracking(Entity target, Entity from) {
        if (target instanceof LivingEntity trackedEntity && from instanceof ServerPlayer player && target instanceof ISilkLeashState trackedState) {
            if (trackedState.getLeashedByEntities().isEmpty() && trackedState.getLeashingEntities().isEmpty()) return;
            CACPacketHandler.sendToPlayer(player,
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

            CACPacketHandler.sendToPlayer(fromPlayer, new ClientboundBubbleStatePacket(bubbleState.isBubbleActive(), trackedPlayer.getId(), OptionalInt.empty()));
            CACPacketHandler.sendToPlayer(fromPlayer, new ClientboundGrapplingStatePacket(grappleState.getHook() != null ? OptionalInt.of(grappleState.getHook().getId()) : OptionalInt.empty(), trackedPlayer.getId()));
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
