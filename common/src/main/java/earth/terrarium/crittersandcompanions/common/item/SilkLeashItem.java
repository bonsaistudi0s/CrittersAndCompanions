package earth.terrarium.crittersandcompanions.common.item;

import earth.terrarium.crittersandcompanions.capability.SilkLeashable;
import earth.terrarium.crittersandcompanions.common.network.NetworkHandler;
import earth.terrarium.crittersandcompanions.common.network.s2c.SilkLeashStatePacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SilkLeashItem extends Item {
    public SilkLeashItem(Properties properties) {
        super(properties);
    }

    public static int updateLeashStates(LivingEntity leashOwner, LivingEntity leashedEntity) {
        List<Entity> updatedStates = updateLeashStatesLocal(leashOwner, leashedEntity);
        if (!updatedStates.isEmpty()) {
            NetworkHandler.CHANNEL.sendToAllLoaded(
                    new SilkLeashStatePacket(
                            updatedStates.stream().map(entity ->
                                    new SilkLeashStatePacket.LeashData(
                                            entity.getId(),
                                            ((SilkLeashable) entity).getLeashingEntities().stream().mapToInt(Entity::getId).boxed().toList(),
                                            ((SilkLeashable) entity).getLeashedByEntities().stream().mapToInt(Entity::getId).boxed().toList()
                                    )
                            ).collect(Collectors.toList())
                    ),
                    leashedEntity.level(),
                    leashedEntity.blockPosition()
            );
        }
        return updatedStates.size();
    }

    public static List<Entity> updateLeashStatesLocal(LivingEntity leashOwner, LivingEntity leashedEntity) {
        if (leashOwner == null && leashedEntity == null) {
            throw new IllegalArgumentException("Both leash members cannot be null");
        }

        List<Entity> modifiedEntities = new ArrayList<>();

        if (leashOwner instanceof SilkLeashable ownerLeashState) {
            if (leashedEntity == null) {
                if (ownerLeashState.getLeashingEntities().isEmpty()) return List.of();

                for (Entity entity : ownerLeashState.getLeashingEntities()) {
                    if (entity instanceof SilkLeashable state) {
                        state.getLeashingEntities().remove(leashedEntity);
                        modifiedEntities.add(entity);
                    };
                }
                ownerLeashState.getLeashingEntities().clear();
            } else {
                if (!canLeash(leashOwner, leashedEntity)) return List.of();

                if (leashedEntity instanceof SilkLeashable state) {
                    state.getLeashingEntities().add(leashOwner);
                    ownerLeashState.getLeashingEntities().add(leashedEntity);
                    modifiedEntities.add(leashedEntity);
                };
            }
            modifiedEntities.add(leashOwner);
        } else if (leashedEntity instanceof SilkLeashable leashedLeashState) {
            if (leashedLeashState.getLeashedByEntities().isEmpty()) {
                return List.of();
            }

            for (Entity entity : leashedLeashState.getLeashedByEntities()) {
                if (entity instanceof SilkLeashable state) {
                    state.getLeashingEntities().remove(leashedEntity);
                    modifiedEntities.add(entity);
                };
            }
            leashedLeashState.getLeashedByEntities().clear();
            modifiedEntities.add(leashedEntity);
        }

        return List.copyOf(modifiedEntities);
    }

    private static boolean canLeash(Entity sourceEntity, Entity targetEntity) {
        if (!(sourceEntity instanceof SilkLeashable sourceLeashState) || !(targetEntity instanceof SilkLeashable targetLeashState)) {
            return false;
        }
        if (sourceEntity == targetEntity) {
            return false;
        }
        return Stream.concat(
                sourceLeashState.getLeashingEntities().stream(),
                sourceLeashState.getLeashedByEntities().stream()
        ).noneMatch(entity -> entity == targetEntity);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack handStack, Player player, LivingEntity entity, InteractionHand interactionHand) {
        if (player instanceof SilkLeashable playerLeashState) {
            if (playerLeashState.getLeashingEntities().isEmpty()) {
                if (!player.getAbilities().instabuild) {
                    handStack.shrink(1);
                }
                if (!player.level().isClientSide()) {
                    SilkLeashItem.updateLeashStates(player, entity);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
