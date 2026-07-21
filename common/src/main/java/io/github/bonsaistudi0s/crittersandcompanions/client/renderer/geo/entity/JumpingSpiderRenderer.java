package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.JumpingSpiderModel;
import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity.base.AgingScaledGeoEntityRenderer;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.JumpingSpiderEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class JumpingSpiderRenderer extends AgingScaledGeoEntityRenderer<JumpingSpiderEntity> {

    public JumpingSpiderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new JumpingSpiderModel());
    }
}
