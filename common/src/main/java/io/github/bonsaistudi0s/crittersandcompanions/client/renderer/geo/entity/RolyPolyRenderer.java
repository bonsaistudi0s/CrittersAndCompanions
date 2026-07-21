package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.RolyPolyModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.RolyPolyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RolyPolyRenderer extends GeoEntityRenderer<RolyPolyEntity> {

    public RolyPolyRenderer(EntityRendererProvider.Context context) {
        super(context, new RolyPolyModel());
        addRenderLayer(new RolyPolyChestLayer(this));
    }

}
