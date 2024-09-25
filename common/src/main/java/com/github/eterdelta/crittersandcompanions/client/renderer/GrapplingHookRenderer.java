package com.github.eterdelta.crittersandcompanions.client.renderer;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.client.model.GrapplingHookModel;
import com.github.eterdelta.crittersandcompanions.entity.GrapplingHookEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GrapplingHookRenderer extends EntityRenderer<GrapplingHookEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CrittersAndCompanions.MODID, "grappling_hook"), "main");
    private static final ResourceLocation TEXTURE = new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/grappling_hook.png");
    private final GrapplingHookModel<GrapplingHookEntity> hookModel;

    public GrapplingHookRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.hookModel = new GrapplingHookModel<>(context.bakeLayer(LAYER_LOCATION));
    }

    private static void stringVertex(float p_174119_, float p_174120_, float p_174121_, VertexConsumer vertexConsumer, PoseStack.Pose pose, float p_174124_, float p_174125_) {
        float f = p_174119_ * p_174124_;
        float f1 = p_174120_ * (p_174124_ * p_174124_ + p_174124_) * 0.5F + 0.25F;
        float f2 = p_174121_ * p_174124_;
        float f3 = p_174119_ * p_174125_ - f;
        float f4 = p_174120_ * (p_174125_ * p_174125_ + p_174125_) * 0.5F + 0.25F - f1;
        float f5 = p_174121_ * p_174125_ - f2;
        float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 /= f6;
        f4 /= f6;
        f5 /= f6;
        vertexConsumer.vertex(pose.pose(), f, f1, f2).color(193, 184, 205, 255).normal(pose.normal(), f3, f4, f5).endVertex();
    }

    public void render(GrapplingHookEntity entity, float p_114706_, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Player player = ((Player) entity.getOwner());
        if (player != null && entity.isFocused()) {
            poseStack.pushPose();

            poseStack.pushPose();
            poseStack.translate(0.0D, -1.25D, 0.0D);
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
            this.hookModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();

            int handOffset = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            if (!player.getMainHandItem().is(entity.getOwnerStack().getItem())) {
                handOffset = -handOffset;
            }

            float f = player.getAttackAnim(partialTicks);
            float f1 = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
            float f2 = Mth.lerp(partialTicks, player.yBodyRotO, player.yBodyRot) * ((float) Math.PI / 180F);
            double d0 = Mth.sin(f2);
            double d1 = Mth.cos(f2);
            double d2 = (double) handOffset * 0.35;
            double d4;
            double d5;
            double d6;
            float f3;
            if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
                double d7 = 960.0D / (double) this.entityRenderDispatcher.options.fov().get().intValue();
                Vec3 vec3 = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) handOffset * 0.825F, -0.08F);
                vec3 = vec3.scale(d7);
                vec3 = vec3.yRot(f1 * 0.5F);
                vec3 = vec3.xRot(-f1 * 0.7F);
                d4 = Mth.lerp(partialTicks, player.xo, player.getX()) + vec3.x;
                d5 = Mth.lerp(partialTicks, player.yo, player.getY()) + vec3.y;
                d6 = Mth.lerp(partialTicks, player.zo, player.getZ()) + vec3.z;
                f3 = player.getEyeHeight();
            } else {
                d4 = Mth.lerp(partialTicks, player.xo, player.getX()) - d1 * d2 - d0 * 0.8D;
                d5 = player.yo + (double) player.getEyeHeight() + (player.getY() - player.yo) * (double) partialTicks - 0.45D;
                d6 = Mth.lerp(partialTicks, player.zo, player.getZ()) - d0 * d2 + d1 * 0.8D;
                f3 = player.isCrouching() ? -0.1875F : 0.0F;
            }

            double hookPosX = Mth.lerp(partialTicks, entity.xo, entity.getX());
            double hookPosY = Mth.lerp(partialTicks, entity.yo, entity.getY()) + 0.4D;
            double hookPosZ = Mth.lerp(partialTicks, entity.zo, entity.getZ());
            float f4 = (float) (d4 - hookPosX);
            float f5 = (float) (d5 - hookPosY) + f3;
            float f6 = (float) (d6 - hookPosZ);
            VertexConsumer stringVertexConsumer = bufferSource.getBuffer(RenderType.lineStrip());
            PoseStack.Pose pose = poseStack.last();

            stringVertex(f4, f5, f6, stringVertexConsumer, pose, 0.0F, 1.0F);
            stringVertex(f4, f5, f6, stringVertexConsumer, pose, 1.0F, 2.0F);

            poseStack.popPose();
            super.render(entity, p_114706_, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(GrapplingHookEntity entity) {
        return TEXTURE;
    }
}
