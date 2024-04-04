package earth.terrarium.crittersandcompanions.common.item;

import earth.terrarium.crittersandcompanions.common.capability.SilkLeashable;
import earth.terrarium.crittersandcompanions.common.network.NetworkHandler;
import earth.terrarium.crittersandcompanions.common.network.s2c.SilkLeashStatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SilkLeashItem extends Item {
    public SilkLeashItem(Properties properties) {
        super(properties);
    }

    public static int updateLeashStates(Level level, BlockPos pos, @Nullable LivingEntity leashOwner, @Nullable LivingEntity leashedEntity) {
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
                level,
                pos
            );
        }
        return updatedStates.size();
    }

    public static List<Entity> updateLeashStatesLocal(LivingEntity leashOwner, LivingEntity leashedEntity) {
        if (leashOwner == null && leashedEntity == null) {
            throw new IllegalArgumentException("Both leash members cannot be null");
        }

        List<Entity> modifiedEntities = new ArrayList<>();

        if (leashOwner == null) {
            SilkLeashable leashedLeashState = (SilkLeashable) leashedEntity;

            if (leashedLeashState.getLeashedByEntities().isEmpty()) {
                return List.of();
            }

            for (Entity entity : leashedLeashState.getLeashedByEntities()) {
                if (entity instanceof SilkLeashable entityLeashState) {
                    entityLeashState.getLeashingEntities().remove(leashedEntity);
                    modifiedEntities.add(entity);
                }
            }

            leashedLeashState.getLeashedByEntities().clear();
            modifiedEntities.add(leashedEntity);
        } else {
            SilkLeashable ownerLeashState = (SilkLeashable) leashOwner;

            if (leashedEntity == null) {
                if (ownerLeashState.getLeashingEntities().isEmpty()) {
                    return List.of();
                }

                for (Entity entity : ownerLeashState.getLeashingEntities()) {
                    if (entity instanceof SilkLeashable entityLeashState) {
                        entityLeashState.getLeashedByEntities().remove(leashOwner);
                        modifiedEntities.add(entity);
                    }
                }
                ownerLeashState.getLeashingEntities().clear();
            } else {
                if (!canLeash(leashOwner, leashedEntity)) {
                    return List.of();
                }

                SilkLeashable leashedEntityLeashState = (SilkLeashable) leashedEntity;

                leashedEntityLeashState.getLeashedByEntities().add(leashOwner);
                ownerLeashState.getLeashingEntities().add(leashedEntity);
                modifiedEntities.add(leashedEntity);
            }
            modifiedEntities.add(leashOwner);
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
                    SilkLeashItem.updateLeashStates(entity.level(), entity.blockPosition(), player, entity);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
