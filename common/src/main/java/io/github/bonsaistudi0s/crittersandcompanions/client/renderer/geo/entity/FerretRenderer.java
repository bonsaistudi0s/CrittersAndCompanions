package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.FerretModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.FerretEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FerretRenderer extends GeoEntityRenderer<FerretEntity> {

    public FerretRenderer(EntityRendererProvider.Context manager) {
        super(manager, new FerretModel());
        addRenderLayer(new FerretOverlayerLayer(this));
    }

}
