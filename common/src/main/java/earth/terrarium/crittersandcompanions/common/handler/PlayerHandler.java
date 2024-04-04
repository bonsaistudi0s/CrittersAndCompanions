package earth.terrarium.crittersandcompanions.common.handler;

import com.google.common.collect.Iterables;
import earth.terrarium.crittersandcompanions.common.capability.Bubbleable;
import earth.terrarium.crittersandcompanions.common.capability.Grapplable;
import earth.terrarium.crittersandcompanions.common.capability.SilkLeashable;
import earth.terrarium.crittersandcompanions.common.network.NetworkHandler;
import earth.terrarium.crittersandcompanions.common.network.s2c.SilkLeashStatePacket;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import earth.terrarium.crittersandcompanions.common.entity.DumboOctopusEntity;
import earth.terrarium.crittersandcompanions.common.entity.KoiFishEntity;
import earth.terrarium.crittersandcompanions.common.item.SilkLeashItem;
import earth.terrarium.crittersandcompanions.common.network.s2c.BubbleStatePacket;
import earth.terrarium.crittersandcompanions.common.network.s2c.GrapplingStatePacket;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class PlayerHandler {

    public static InteractionHand getOppositeHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    public static void onPlayerEntityInteract(Player player, InteractionHand hand, Entity target, Consumer<Boolean> cancelAction, Consumer<InteractionResult> resultAction) {
        if (!player.level().isClientSide() && target instanceof LivingEntity entity) {
            ItemStack handStack = player.getItemInHand(hand);
            ItemStack otherHandStack = player.getItemInHand(getOppositeHand(hand));

            if (player instanceof SilkLeashable playerLeashState) {
                Set<LivingEntity> playerLeashingEntities = playerLeashState.getLeashingEntities();

                if (!otherHandStack.is(ModItems.SILK_LEAD.get())) {
                    if ((playerLeashingEntities.isEmpty() || playerLeashingEntities.contains(entity))
                        && !(handStack.is(ModItems.SILK_LEAD.get()) || handStack.is(Items.LEAD))
                        && hand == InteractionHand.MAIN_HAND) {
                        int unleashedStates = 0;
                        unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates(entity.level(), entity.blockPosition(), entity, null) - 1);
                        unleashedStates += Math.max(0, SilkLeashItem.updateLeashStates(entity.level(), entity.blockPosition(), null, entity) - 1);
                        if (unleashedStates > 0) {
                            ItemEntity leadEntity = new ItemEntity(player.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ModItems.SILK_LEAD.get(), unleashedStates));
                            player.level().addFreshEntity(leadEntity);
                            cancelAction.accept(true);
                            resultAction.accept(InteractionResult.SUCCESS);
                        }
                    } else {
                        LivingEntity uniqueLeash = Iterables.getFirst(playerLeashingEntities, null);
                        if (uniqueLeash != null) {
                            if (SilkLeashItem.updateLeashStates(entity.level(), entity.blockPosition(), uniqueLeash, entity) != 0) {
                                SilkLeashItem.updateLeashStates(entity.level(), entity.blockPosition(), player, null);
                                cancelAction.accept(true);
                                resultAction.accept(InteractionResult.SUCCESS);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void onPlayerTick(Player player) {
        if (!player.level().isClientSide()) {
            List<KoiFishEntity> nearKoiFishes = player.level().getEntitiesOfClass(KoiFishEntity.class, player.getBoundingBox().inflate(10.0D), EntitySelector.ENTITY_STILL_ALIVE);

            if (nearKoiFishes.size() >= 3) {
                player.addEffect(new MobEffectInstance(MobEffects.LUCK, 210, 0, false, false));
            }
        }
    }

    public static void onPlayerStartTracking(Player player, Entity target) {
        if (target instanceof SilkLeashable trackedState) {
            NetworkHandler.CHANNEL.sendToPlayer(
                new SilkLeashStatePacket(
                    new SilkLeashStatePacket.LeashData(
                        target.getId(),
                        trackedState.getLeashingEntities().stream().mapToInt(Entity::getId).boxed().toList(),
                        trackedState.getLeashedByEntities().stream().mapToInt(Entity::getId).boxed().toList()
                    )
                ), player);
        }

        if (target instanceof Bubbleable bubbleCap) {
            NetworkHandler.CHANNEL.sendToPlayer(new BubbleStatePacket(target.getId(), bubbleCap.crittersandcompanions$isActive()), player);
        }

        if (target instanceof Grapplable grappleCap) {
            NetworkHandler.CHANNEL.sendToPlayer(new GrapplingStatePacket(target.getId(), grappleCap.crittersandcompanions$getHook() == null ? Optional.empty() : Optional.of(grappleCap.crittersandcompanions$getHook().getId())), player);
        }
    }

    public static void onPlayerStopTracking(Player player, Entity target) {
        if (target instanceof DumboOctopusEntity dumboOctopus) {
            if (dumboOctopus.getBubbledPlayer() == player) {
                dumboOctopus.sendBubble((ServerPlayer) player, false);
            }
        }
    }
}
