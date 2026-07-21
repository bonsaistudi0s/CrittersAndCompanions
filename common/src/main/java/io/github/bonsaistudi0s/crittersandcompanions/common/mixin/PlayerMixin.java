package io.github.bonsaistudi0s.crittersandcompanions.common.mixin;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.GrapplingHookEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IBubbleState;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IGrapplingState;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public class PlayerMixin implements IBubbleState, IGrapplingState {

    @Unique
    private GrapplingHookEntity hook;

    @Unique
    private boolean bubbleActive;

    @Override
    public GrapplingHookEntity getHook() {
        return this.hook;
    }

    @Override
    public void setHook(GrapplingHookEntity hook) {
        this.hook = hook;
    }

    @Override
    public boolean isBubbleActive() {
        return this.bubbleActive;
    }

    @Override
    public void setBubbleActive(boolean active) {
        this.bubbleActive = active;
    }

}
