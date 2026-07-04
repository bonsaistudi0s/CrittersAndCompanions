package io.github.bonsaistudi0s.crittersandcompanions.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.SilkLeashRenderer;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    public LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(at = @At("TAIL"), method = "render*")
    private void onRender(T entity, float p_115456_, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int p_115460_, CallbackInfo callback) {
        SilkLeashRenderer.renderSilkLeash(entity, partialTicks, poseStack, bufferSource);
    }

}
