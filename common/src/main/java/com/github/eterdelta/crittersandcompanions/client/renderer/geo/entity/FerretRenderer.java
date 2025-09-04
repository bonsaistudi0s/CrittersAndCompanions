package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.client.model.geo.FerretModel;
import com.github.eterdelta.crittersandcompanions.entity.FerretEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FerretRenderer extends GeoEntityRenderer<FerretEntity> {

    public FerretRenderer(EntityRendererProvider.Context manager) {
        super(manager, new FerretModel());
        addRenderLayer(new FerretOverlayerLayer(this));
    }

}
