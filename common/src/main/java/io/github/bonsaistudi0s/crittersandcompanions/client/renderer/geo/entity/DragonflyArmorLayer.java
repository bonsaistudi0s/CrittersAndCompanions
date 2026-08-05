package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.DragonflyEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.item.DragonflyArmorItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class DragonflyArmorLayer extends GeoRenderLayer<DragonflyEntity> {

    public DragonflyArmorLayer(GeoRenderer<DragonflyEntity> renderer) {
        super(renderer);
    }

    @Override
    @Nullable
    protected ResourceLocation getTextureResource(DragonflyEntity animatable) {
        if (animatable.getArmor().getItem() instanceof DragonflyArmorItem item) {
            return item.getTexture();
        }
        return null;
    }

    @Override
    public void render(PoseStack poseStack, DragonflyEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        var texture = getTextureResource(animatable);
        if (texture == null) {
            return;
        }

        renderType = RenderType.entityCutoutNoCull(texture);
        buffer = bufferSource.getBuffer(renderType);
        var color = getRenderer().getRenderColor(animatable, partialTick, packedLight);
        getRenderer().reRender(
                bakedModel,
                poseStack,
                bufferSource,
                animatable,
                renderType,
                buffer,
                partialTick,
                packedLight,
                packedOverlay,
                color.getRedFloat(),
                color.getGreenFloat(),
                color.getBlueFloat(),
                color.getAlphaFloat()
        );
    }
}
