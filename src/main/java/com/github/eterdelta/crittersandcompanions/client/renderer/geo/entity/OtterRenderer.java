package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.client.model.geo.OtterModel;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class OtterRenderer extends GeoEntityRenderer<OtterEntity> {
    public OtterRenderer(EntityRendererProvider.Context context) {
        super(context, new OtterModel());
    }

    @Override
    public RenderType getRenderType(OtterEntity animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("held_item")) {
            stack.pushPose();
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.translate(0.05D, 0.2D, -0.9D);
            stack.mulPose(Quaternion.fromXYZ(1.5708F, 1.5708F, 0.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(this.mainHand, ItemTransforms.TransformType.FIXED, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();

            bufferIn = rtb.getBuffer(RenderType.entityCutoutNoCull(this.whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}