package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.AgingVariantGeoModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.StickBugEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StickBugRenderer extends GeoEntityRenderer<StickBugEntity> {

    public StickBugRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgingVariantGeoModel<>(CrittersAndCompanions.createId("stick_bug")));
    }

    @Override
    public float getMotionAnimThreshold(StickBugEntity animatable) {
        return 0.01f;
    }
}
