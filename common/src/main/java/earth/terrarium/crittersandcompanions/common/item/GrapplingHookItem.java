package earth.terrarium.crittersandcompanions.common.item;

import earth.terrarium.crittersandcompanions.common.capability.Grapplable;
import earth.terrarium.crittersandcompanions.common.entity.GrapplingHookEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class GrapplingHookItem extends Item {
    public GrapplingHookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);

        if (player instanceof Grapplable grapplingState) {
            if (grapplingState.getHook() != null) {
                grapplingState.getHook().pull();
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.25F, 1.0F + 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            } else {
                if (!level.isClientSide()) {
                    GrapplingHookEntity hook = new GrapplingHookEntity(player, stack, level);
                    hook.setDeltaMovement(player.getLookAngle().scale(1.5D));
                    level.addFreshEntity(hook);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.25F, 1.0F + 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                player.gameEvent(GameEvent.ITEM_INTERACT_START);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
