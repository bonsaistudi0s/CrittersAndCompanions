package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.client.model.geo.JumpingSpiderModel;
import com.github.eterdelta.crittersandcompanions.entity.JumpingSpiderEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class JumpingSpiderRenderer extends GeoEntityRenderer<JumpingSpiderEntity> {
    public JumpingSpiderRenderer(EntityRendererProvider.Context context) {
        super(context, new JumpingSpiderModel());
    }

    @Override
    public RenderType getRenderType(JumpingSpiderEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
