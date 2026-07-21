package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity.base.AgingScaledGeoEntityRenderer;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.LadybugEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class LadybugRenderer extends AgingScaledGeoEntityRenderer<LadybugEntity> {

    public LadybugRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(CrittersAndCompanions.createId("ladybug")));
    }
}
