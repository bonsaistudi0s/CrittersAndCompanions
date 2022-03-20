package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DragonflyModel extends AnimatedGeoModel<DragonflyEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "geo/dragonfly.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/dragonfly.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(CrittersAndCompanions.MODID, "animations/dragonfly.animation.json");

    @Override
    public ResourceLocation getModelLocation(DragonflyEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(DragonflyEntity object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DragonflyEntity animatable) {
        return ANIMATION;
    }
}
