package com.github.eterdelta.crittersandcompanions.entity.brain.behaviour;

import com.github.eterdelta.crittersandcompanions.entity.SnailEntity;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import com.github.eterdelta.crittersandcompanions.registry.AnimalTags;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

public record TameableBehaviour(TamableAnimal owner, AnimalTags tags) implements Behaviour {

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(owner.isSleeping()) return InteractionResult.PASS;

        var stack = player.getItemInHand(hand);

        if (owner.isTame()) {
            if (owner.isOwnedBy(player)) {
                var feedResult = heal(player, stack);
                if (feedResult != InteractionResult.PASS) return feedResult;

                return order(stack);
            }
        } else {
            return tame(player, stack);
        }

        return InteractionResult.PASS;
    }

    private InteractionResult order(ItemStack stack) {
        if (stack.is(tags.food())) return InteractionResult.PASS;
        if (stack.is(tags.tempt())) return InteractionResult.PASS;

        owner.setOrderedToSit(!owner.isOrderedToSit());
        return sucess();
    }

    private InteractionResult heal(Player player, ItemStack stack) {
        if (!stack.is(tags.food())) return InteractionResult.PASS;

        if (owner.getHealth() >= owner.getMaxHealth()) return InteractionResult.PASS;

        if (owner.level().isClientSide()) {
            return InteractionResult.CONSUME;
        }

        owner.gameEvent(GameEvent.EAT, owner);
        owner.heal(2.0F);

        stack.consume(1, player);

        if (owner instanceof SnailEntity snail && snail.isGaryVariant()) {
            owner.playSound(CACSounds.SNAIL_GARY_PURR.get(), 0.5F, 1.0F);
        }

        return sucess();
    }

    private InteractionResult tame(Player player, ItemStack stack) {
        if (!stack.is(tags.tempt())) return InteractionResult.PASS;

        stack.consume(1, player);

        if (!owner.level().isClientSide()) {
            if (owner.getRandom().nextInt(10) == 0 && Services.EVENTS.canTame(owner, player)) {
                owner.tame(player);
                owner.level().broadcastEntityEvent(owner, (byte) 7);

                if (owner instanceof SnailEntity snail && snail.isGaryVariant()) {
                    owner.playSound(CACSounds.SNAIL_GARY_PURR.get(), 0.5F, 1.0F);
                }
            } else {
                owner.level().broadcastEntityEvent(owner, (byte) 6);
            }
        }

        return sucess();
    }

    private InteractionResult sucess() {
        return InteractionResult.sidedSuccess(owner.level().isClientSide());
    }

}
