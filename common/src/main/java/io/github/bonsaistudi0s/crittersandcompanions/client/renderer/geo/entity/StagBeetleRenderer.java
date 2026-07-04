package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.VariantGeoModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.StagBeetleEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StagBeetleRenderer extends GeoEntityRenderer<StagBeetleEntity> {

    public StagBeetleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VariantGeoModel<>(CrittersAndCompanions.createId("stag_beetle")));
    }

    @Override
    protected float getDeathMaxRotation(StagBeetleEntity animatable) {
        return 0.0F;
    }
}
