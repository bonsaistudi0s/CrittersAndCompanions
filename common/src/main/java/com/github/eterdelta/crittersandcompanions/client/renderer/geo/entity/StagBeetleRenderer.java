package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.entity.StagBeetleEntity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StagBeetleRenderer extends GeoEntityRenderer<StagBeetleEntity> {

    public StagBeetleRenderer(EntityRendererProvider.Context renderManager, GeoModel<StagBeetleEntity> model) {
        super(renderManager, model);
    }

    @Override
    protected float getDeathMaxRotation(StagBeetleEntity animatable) {
        return 0.0F;
    }
}
