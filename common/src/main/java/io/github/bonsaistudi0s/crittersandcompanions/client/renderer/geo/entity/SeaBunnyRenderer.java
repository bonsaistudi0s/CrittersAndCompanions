package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.SeaBunnyModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.SeaBunnyEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SeaBunnyRenderer extends GeoEntityRenderer<SeaBunnyEntity> {

    public SeaBunnyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SeaBunnyModel());
    }
}
