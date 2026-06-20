package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.entity.RolyPolyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RolyPolyRenderer extends GeoEntityRenderer<RolyPolyEntity> {

    public RolyPolyRenderer(EntityRendererProvider.Context context, GeoModel<RolyPolyEntity> model) {
        super(context, model);
        addRenderLayer(new RolyPolyChestLayer(this));
    }

}
