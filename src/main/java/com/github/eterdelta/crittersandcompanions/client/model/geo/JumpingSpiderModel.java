package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.JumpingSpiderEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class JumpingSpiderModel extends AnimatedGeoModel<JumpingSpiderEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "geo/jumping_spider.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/jumping_spider.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(CrittersAndCompanions.MODID, "animations/jumping_spider.animation.json");

    @Override
    public ResourceLocation getModelResource(JumpingSpiderEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(JumpingSpiderEntity object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(JumpingSpiderEntity animatable) {
        return ANIMATION;
    }
}
