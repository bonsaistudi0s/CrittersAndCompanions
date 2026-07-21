package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.SeaBunnyModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.SeaBunnyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SeaBunnyRenderer extends GeoEntityRenderer<SeaBunnyEntity> {

    public SeaBunnyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SeaBunnyModel());
    }
}
