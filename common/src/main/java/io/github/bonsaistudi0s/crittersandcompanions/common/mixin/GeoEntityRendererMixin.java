package io.github.bonsaistudi0s.crittersandcompanions.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.SilkLeashRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin {

    @Inject(
            at = @At("TAIL"),
            method = "render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    )
    private void onRender(Entity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo callback) {
        SilkLeashRenderer.renderSilkLeash(entity, partialTick, poseStack, bufferSource);
    }
}
