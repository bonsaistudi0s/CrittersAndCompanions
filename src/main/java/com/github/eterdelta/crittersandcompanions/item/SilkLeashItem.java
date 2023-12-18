package com.github.eterdelta.crittersandcompanions.item;

import com.github.eterdelta.crittersandcompanions.capability.CACCapabilities;
import com.github.eterdelta.crittersandcompanions.capability.ISilkLeashStateCapability;
import com.github.eterdelta.crittersandcompanions.entity.ILeashStateEntity;
import com.github.eterdelta.crittersandcompanions.network.CACPacketHandler;
import com.github.eterdelta.crittersandcompanions.network.ClientboundSilkLeashStatePacket;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SilkLeashItem extends Item {
    public SilkLeashItem(Properties properties) {
        super(properties);
    }

    public static int updateLeashStates(LivingEntity leashOwner, LivingEntity leashedEntity) {
        Map<Entity, ISilkLeashStateCapability> updatedStates = updateLeashStatesLocal(leashOwner, leashedEntity);
        if (!updatedStates.isEmpty()) {
            CACPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> leashOwner == null ? leashedEntity : leashOwner),
                    new ClientboundSilkLeashStatePacket(
                            updatedStates.entrySet().stream().map(entry ->
                                    new ClientboundSilkLeashStatePacket.LeashData(
                                            entry.getKey().getId(),
                                            new IntArrayList(entry.getValue().getLeashingEntities().stream().mapToInt(Entity::getId).toArray()),
                                            new IntArrayList(entry.getValue().getLeashedByEntities().stream().mapToInt(Entity::getId).toArray())
                                    )
                            ).collect(Collectors.toList())
                    )
            );
        }
        return updatedStates.size();
    }

    public static Map<Entity, ISilkLeashStateCapability> updateLeashStatesLocal(LivingEntity leashOwner, LivingEntity leashedEntity) {
        if (leashOwner == null && leashedEntity == null) {
            throw new IllegalArgumentException("Both leash members cannot be null");
        }

        Map<Entity, ISilkLeashStateCapability> modifiedEntities = new HashMap<>();

        if (leashOwner == null) {
            LazyOptional<ISilkLeashStateCapability> leashedCap = ((ILeashStateEntity) leashedEntity).getLeashStateCache();
            ISilkLeashStateCapability leashedLeashState = leashedCap.orElseThrow(IllegalStateException::new);

            if (leashedLeashState.getLeashedByEntities().isEmpty()) {
                return Map.of();
            }

            for (Entity entity : leashedLeashState.getLeashedByEntities()) {
                entity.getCapability(CACCapabilities.SILK_LEASH_STATE).ifPresent(state -> {
                    state.getLeashingEntities().remove(leashedEntity);
                    modifiedEntities.put(entity, state);
                });
            }
            leashedLeashState.getLeashedByEntities().clear();
            modifiedEntities.put(leashedEntity, leashedLeashState);
        } else {
            LazyOptional<ISilkLeashStateCapability> ownerCap = ((ILeashStateEntity) leashOwner).getLeashStateCache();
            ISilkLeashStateCapability ownerLeashState = ownerCap.orElseThrow(IllegalStateException::new);

            if (leashedEntity == null) {
                if (ownerLeashState.getLeashingEntities().isEmpty()) {
                    return Map.of();
                }

                for (Entity entity : ownerLeashState.getLeashingEntities()) {
                    entity.getCapability(CACCapabilities.SILK_LEASH_STATE).ifPresent(state -> {
                        state.getLeashedByEntities().remove(leashOwner);
                        modifiedEntities.put(entity, state);
                    });
                }
                ownerLeashState.getLeashingEntities().clear();
            } else {
                if (!canLeash(leashOwner, leashedEntity)) {
                    return Map.of();
                }

                LazyOptional<ISilkLeashStateCapability> leashedEntityCap = ((ILeashStateEntity) leashedEntity).getLeashStateCache();
                leashedEntityCap.ifPresent(leashedEntityLeashState -> {
                    leashedEntityLeashState.getLeashedByEntities().add(leashOwner);
                    ownerLeashState.getLeashingEntities().add(leashedEntity);
                    modifiedEntities.put(leashedEntity, leashedEntityLeashState);
                });
            }
            modifiedEntities.put(leashOwner, ownerLeashState);
        }

        return Map.copyOf(modifiedEntities);
    }

    private static boolean canLeash(Entity sourceEntity, Entity targetEntity) {
        return sourceEntity.getCapability(CACCapabilities.SILK_LEASH_STATE).map(sourceLeashState -> {
            if (sourceEntity == targetEntity) {
                return false;
            }
            return Stream.concat(
                    sourceLeashState.getLeashingEntities().stream(),
                    sourceLeashState.getLeashedByEntities().stream()
            ).noneMatch(entity -> entity == targetEntity);
        }).orElse(false);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack handStack, Player player, LivingEntity entity, InteractionHand interactionHand) {
        LazyOptional<ISilkLeashStateCapability> playerLeashCap = player.getCapability(CACCapabilities.SILK_LEASH_STATE);

        if (playerLeashCap.isPresent()) {
            LazyOptional<ISilkLeashStateCapability> entityLeashCap = entity.getCapability(CACCapabilities.SILK_LEASH_STATE);

            if (entityLeashCap.isPresent() && handStack.is(CACItems.SILK_LEAD.get())) {
                ISilkLeashStateCapability playerLeashState = playerLeashCap.orElse(null);

                if (playerLeashState.getLeashingEntities().isEmpty()) {
                    if (!player.getAbilities().instabuild) {
                        handStack.shrink(1);
                    }
                    if (!player.getLevel().isClientSide()) {
                        SilkLeashItem.updateLeashStates(player, entity);
                        return InteractionResult.CONSUME;
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
