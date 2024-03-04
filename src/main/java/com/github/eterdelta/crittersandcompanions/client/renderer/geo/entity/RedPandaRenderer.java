package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.client.model.geo.RedPandaModel;
import com.github.eterdelta.crittersandcompanions.entity.RedPandaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RedPandaRenderer extends GeoEntityRenderer<RedPandaEntity> {
    public RedPandaRenderer(EntityRendererProvider.Context context) {
        super(context, new RedPandaModel());
    }

    @Override
    public RenderType getRenderType(RedPandaEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }

}
