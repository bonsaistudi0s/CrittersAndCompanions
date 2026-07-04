package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.ShimaEnagaModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.ShimaEnagaEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ShimaEnagaRenderer extends GeoEntityRenderer<ShimaEnagaEntity> {

    public ShimaEnagaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShimaEnagaModel());
    }
}
