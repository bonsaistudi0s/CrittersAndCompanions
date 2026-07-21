package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.VariantGeoModel;
import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity.base.AgingScaledGeoEntityRenderer;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.StagBeetleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class StagBeetleRenderer extends AgingScaledGeoEntityRenderer<StagBeetleEntity> {

    public StagBeetleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VariantGeoModel<>(CrittersAndCompanions.createId("stag_beetle")));
    }

    @Override
    protected float getDeathMaxRotation(StagBeetleEntity animatable) {
        return 0.0F;
    }
}
