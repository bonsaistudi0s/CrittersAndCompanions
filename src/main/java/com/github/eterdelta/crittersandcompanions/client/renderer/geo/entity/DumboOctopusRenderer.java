package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.client.model.geo.DumboOctopusModel;
import com.github.eterdelta.crittersandcompanions.entity.DumboOctopusEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DumboOctopusRenderer extends GeoEntityRenderer<DumboOctopusEntity> {

    public DumboOctopusRenderer(EntityRendererProvider.Context context) {
        super(context, new DumboOctopusModel());
    }

    @Override
    public RenderType getRenderType(DumboOctopusEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }

}
