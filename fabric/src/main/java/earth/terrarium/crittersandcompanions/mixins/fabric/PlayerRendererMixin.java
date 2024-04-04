package earth.terrarium.crittersandcompanions.mixins.fabric;

import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.client.renderer.BubbleLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
        super(context, entityModel, f); // no-op
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addBubbleLayer(EntityRendererProvider.Context context, boolean bl, CallbackInfo ci) {
        try {
            LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> skinRenderer = this;
            this.addLayer(new BubbleLayer(skinRenderer, context.getModelSet()));
        } catch (Exception e) {
            CrittersAndCompanions.LOGGER.log(Level.ERROR, "Failed to add bubble layer to player renderer", e);
        }
    }
}