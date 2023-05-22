package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.ShimaEnagaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ShimaEnagaModel extends AnimatedGeoModel<ShimaEnagaEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "geo/shima_enaga.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/shima_enaga.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(CrittersAndCompanions.MODID, "animations/shima_enaga.animation.json");

    @Override
    public ResourceLocation getModelResource(ShimaEnagaEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(ShimaEnagaEntity object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(ShimaEnagaEntity animatable) {
        return ANIMATION;
    }
}
