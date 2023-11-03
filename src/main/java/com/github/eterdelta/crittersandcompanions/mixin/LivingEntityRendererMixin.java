package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.capability.ISilkLeashStateCapability;
import com.github.eterdelta.crittersandcompanions.entity.ILeashStateEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    public LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void onRender(T entity, float p_115456_, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int p_115460_, CallbackInfo callback) {
        LazyOptional<ISilkLeashStateCapability> silkLeashCap = ((ILeashStateEntity) entity).getLeashStateCache();
        silkLeashCap.ifPresent(capability -> {
            Set<LivingEntity> leashedByEntities = capability.getLeashedByEntities();
            for (LivingEntity leashedBy : leashedByEntities) {
                this.renderSilkLeash(entity, partialTicks, poseStack, bufferSource, leashedBy);
            }
        });
    }

    @Unique
    private static void addVertexPair(VertexConsumer vertexConsumer, Matrix4f matrix4f, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174315_, int p_174316_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_, float gradient) {
        float f = (float) p_174321_ / 24.0F;
        int i = (int) Mth.lerp(f, (float) p_174313_, (float) 1);
        int j = (int) Mth.lerp(f, (float) p_174315_, (float) p_174316_);
        int k = LightTexture.pack(i, j);
        float f1 = p_174321_ % 2 == (p_174322_ ? 1 : 0) ? 0.7F : 1.0F;
        float r = 0.75F * f1 * gradient;
        float g = 0.72F * f1 * gradient;
        float b = 0.8F * f1 * gradient;
        float f5 = p_174310_ * f;
        float f6 = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float f7 = p_174312_ * f;
        vertexConsumer.vertex(matrix4f, f5 - p_174319_, f6 + p_174318_, f7 + p_174320_).color(r, g, b, 1.0F).uv2(k).endVertex();
        vertexConsumer.vertex(matrix4f, f5 + p_174319_, f6 + (float) 0.025 - p_174318_, f7 - p_174320_).color(r, g, b, 1.0F).uv2(k).endVertex();
    }

    @Unique
    private <E extends Entity> void renderSilkLeash(T entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, E leashedToEntity) {
        poseStack.pushPose();
        Vec3 vec3 = leashedToEntity.getRopeHoldPosition(partialTicks);
        double d0 = (double) (Mth.lerp(partialTicks, entity.yBodyRotO, entity.yBodyRot) * ((float) Math.PI / 180F)) + (Math.PI / 2D);
        Vec3 vec31 = entity.getLeashOffset();
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp(partialTicks, entity.xo, entity.getX()) + d1;
        double d4 = Mth.lerp(partialTicks, entity.yo, entity.getY()) + vec31.y;
        double d5 = Mth.lerp(partialTicks, entity.zo, entity.getZ()) + d2;
        poseStack.translate(d1, vec31.y, d2);
        float f = (float) (vec3.x - d3);
        float f1 = (float) (vec3.y - d4);
        float f2 = (float) (vec3.z - d5);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.leash());
        Matrix4f matrix4f = poseStack.last().pose();
        float f4 = Mth.fastInvSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = new BlockPos(entity.getEyePosition(partialTicks));
        BlockPos blockpos1 = new BlockPos(leashedToEntity.getEyePosition(partialTicks));
        int i = this.getBlockLightLevel(entity, blockpos);
        int k = entity.level.getBrightness(LightLayer.SKY, blockpos);
        int l = entity.level.getBrightness(LightLayer.SKY, blockpos1);

        for (int i1 = 0; i1 <= 24; ++i1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, k, l, 0.025F, f5, f6, i1, false, 0.25F + 0.75F * (i1 / 24.0F));
        }
        for (int j1 = 24; j1 >= 0; --j1) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, k, l, 0.0F, f5, f6, j1, true, 0.25F + 0.75F * (j1 / 24.0F));
        }

        poseStack.popPose();
    }
}
