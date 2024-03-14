package earth.terrarium.crittersandcompanions.client.renderer.geo.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import earth.terrarium.crittersandcompanions.client.model.geo.OtterModel;
import earth.terrarium.crittersandcompanions.common.entity.OtterEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;
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
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.translate(0.05D, 0.2D, -0.9D);
            stack.mulPose(new Quaternionf(1.5708F, 1.5708F, 0.0F, 1));
            Minecraft.getInstance().getItemRenderer().renderStatic(animatable.getMainHandItem(), ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, animatable.level(), 0);
            stack.popPose();

            buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getGeoModel().getTextureResource(animatable)));
        }
        super.renderRecursively(stack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}