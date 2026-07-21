package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.VariantGeoModel;
import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity.base.AgingScaledGeoEntityRenderer;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.StickBugEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StickBugRenderer extends AgingScaledGeoEntityRenderer<StickBugEntity> {

    public StickBugRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VariantGeoModel<>(CrittersAndCompanions.createId("stick_bug")));
    }
}
