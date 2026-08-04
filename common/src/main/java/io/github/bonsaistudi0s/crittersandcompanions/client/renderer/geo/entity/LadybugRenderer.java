package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.AgingGeoModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.LadybugEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LadybugRenderer extends GeoEntityRenderer<LadybugEntity> {

    public LadybugRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgingGeoModel<>(CrittersAndCompanions.createId("ladybug")));
    }
}
