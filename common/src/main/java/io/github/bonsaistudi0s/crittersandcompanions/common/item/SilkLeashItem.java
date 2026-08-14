package io.github.bonsaistudi0s.crittersandcompanions.common.item;

import io.github.bonsaistudi0s.crittersandcompanions.common.extension.ISilkLeashState;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.CACPacketHandler;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.ClientboundSilkLeashStatePacket;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SilkLeashItem extends Item {
    public SilkLeashItem(Properties properties) {
        super(properties);
    }

    public static int updateLeashStates(LivingEntity leashOwner, LivingEntity leashedEntity) {
         Map<Entity, ISilkLeashState> updatedStates = updateLeashStatesLocal(leashOwner, leashedEntity);
        if (!updatedStates.isEmpty()) {
            var packet = new ClientboundSilkLeashStatePacket(
                    updatedStates.entrySet().stream().map(entry ->
                            new ClientboundSilkLeashStatePacket.LeashData(
                                    entry.getKey().getId(),
                                    new IntArrayList(entry.getValue().getLeashingEntities().stream().mapToInt(Entity::getId).toArray()),
                                    new IntArrayList(entry.getValue().getLeashedByEntities().stream().mapToInt(Entity::getId).toArray())
                            )
                    ).collect(Collectors.toList())
            );

            if (leashOwner != null) {
                CACPacketHandler.sendToTrackingAndSelf(leashOwner, packet);
            }

            if (leashedEntity != null) {
                CACPacketHandler.sendToTrackingAndSelf(leashedEntity, packet);
            }
        }

        return updatedStates.size();
    }

    public static Map<Entity, ISilkLeashState> updateLeashStatesLocal(LivingEntity leashOwner, LivingEntity leashedEntity) {
        if (leashOwner == null && leashedEntity == null) {
            throw new IllegalArgumentException("Both leash members cannot be null");
        }

        Map<Entity, ISilkLeashState> modifiedEntities = new HashMap<>();

        if (leashOwner == null) {
            var leashedLeashState = (ISilkLeashState) leashedEntity;

            if (leashedLeashState.getLeashedByEntities().isEmpty()) {
                return Map.of();
            }

            for (Entity entity : leashedLeashState.getLeashedByEntities()) {
                if (entity instanceof ISilkLeashState state) {
                    state.getLeashingEntities().remove(leashedEntity);
                    modifiedEntities.put(entity, state);
                }
            }
            leashedLeashState.getLeashedByEntities().clear();
            modifiedEntities.put(leashedEntity, leashedLeashState);
        } else {
            var ownerLeashState = (ISilkLeashState) leashOwner;

            if (leashedEntity == null) {
                if (ownerLeashState.getLeashingEntities().isEmpty()) {
                    return Map.of();
                }

                for (Entity entity : ownerLeashState.getLeashingEntities()) {
                    var state = (ISilkLeashState) entity;
                    state.getLeashedByEntities().remove(leashOwner);
                    modifiedEntities.put(entity, state);
                }
                ownerLeashState.getLeashingEntities().clear();
            } else {
                if (!canLeash(leashOwner, leashedEntity)) {
                    return Map.of();
                }

                var leashedEntityLeashState = (ISilkLeashState) leashedEntity;
                leashedEntityLeashState.getLeashedByEntities().add(leashOwner);
                ownerLeashState.getLeashingEntities().add(leashedEntity);
                modifiedEntities.put(leashedEntity, leashedEntityLeashState);
            }
            modifiedEntities.put(leashOwner, ownerLeashState);
        }

        return Map.copyOf(modifiedEntities);
    }

    private static boolean canLeash(Entity sourceEntity, Entity targetEntity) {
        if (!(sourceEntity instanceof ISilkLeashState sourceLeashState)) return false;
        if (sourceEntity == targetEntity) return false;

        return Stream.concat(
                sourceLeashState.getLeashingEntities().stream(),
                sourceLeashState.getLeashedByEntities().stream()
        ).noneMatch(entity -> entity == targetEntity);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack handStack, Player player, LivingEntity entity, InteractionHand interactionHand) {
        var playerLeashState = (ISilkLeashState) player;

        if (handStack.is(CACItems.SILK_LEAD.get())) {
            if (playerLeashState.getLeashingEntities().isEmpty()) {
                if (!player.getAbilities().instabuild) {
                    handStack.shrink(1);
                }
                if (!player.level().isClientSide()) {
                    SilkLeashItem.updateLeashStates(player, entity);
                    return InteractionResult.CONSUME;
                } else {
                    SilkLeashItem.updateLeashStatesLocal(player, entity);
                }
                return InteractionResult.SUCCESS;
            }

        }

        return InteractionResult.PASS;
    }
}
