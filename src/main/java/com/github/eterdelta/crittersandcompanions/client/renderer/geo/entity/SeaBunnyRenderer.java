package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.client.model.geo.SeaBunnyModel;
import com.github.eterdelta.crittersandcompanions.entity.SeaBunnyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SeaBunnyRenderer extends GeoEntityRenderer<SeaBunnyEntity> {
    public SeaBunnyRenderer(EntityRendererProvider.Context context) {
        super(context, new SeaBunnyModel());
    }

    @Override
    public RenderType getRenderType(SeaBunnyEntity animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
