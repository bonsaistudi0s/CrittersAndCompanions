package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.AgingGeoModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.WeevilEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WeevilRenderer extends GeoEntityRenderer<WeevilEntity> {

    public WeevilRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgingGeoModel<>(CrittersAndCompanions.createId("weevil")));
    }
}
