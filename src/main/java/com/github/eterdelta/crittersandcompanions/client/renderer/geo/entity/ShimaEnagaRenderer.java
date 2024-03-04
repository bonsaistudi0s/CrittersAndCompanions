package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.client.model.geo.ShimaEnagaModel;
import com.github.eterdelta.crittersandcompanions.entity.ShimaEnagaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ShimaEnagaRenderer extends GeoEntityRenderer<ShimaEnagaEntity> {
    public ShimaEnagaRenderer(EntityRendererProvider.Context context) {
        super(context, new ShimaEnagaModel());
    }

    @Override
    public RenderType getRenderType(ShimaEnagaEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }

}
