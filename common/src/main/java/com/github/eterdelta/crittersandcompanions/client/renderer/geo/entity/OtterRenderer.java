package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.client.model.geo.OtterModel;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OtterRenderer extends GeoEntityRenderer<OtterEntity> {
    public OtterRenderer(EntityRendererProvider.Context context) {
        super(context, new OtterModel());
    }

    @Override
    public void renderRecursively(PoseStack stack, OtterEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("held_item")) {
            stack.pushPose();
            stack.scale(0.5f, 0.5f, 0.5f);
            stack.translate(0.05D, 0.2D, -0.9D);
            stack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            stack.mulPose(Axis.YP.rotationDegrees(180.0F));
            stack.translate(0.0D, -0.125D, 0.0D);
            Minecraft.getInstance().getItemRenderer().renderStatic(animatable.getMainHandItem(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, packedOverlay, stack, bufferSource, animatable.level(), 0);
            stack.popPose();

            buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getGeoModel().getTextureResource(animatable)));
        }
        super.renderRecursively(stack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}