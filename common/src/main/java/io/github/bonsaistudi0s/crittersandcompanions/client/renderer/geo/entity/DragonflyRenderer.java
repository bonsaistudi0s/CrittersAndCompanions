package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.DragonflyModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.DragonflyEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DragonflyRenderer extends GeoEntityRenderer<DragonflyEntity> {

    public DragonflyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DragonflyModel());
    }
}
