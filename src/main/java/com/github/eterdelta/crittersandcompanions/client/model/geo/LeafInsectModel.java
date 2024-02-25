package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.LeafInsectEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;


public class LeafInsectModel extends GeoModel<LeafInsectEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "geo/leaf_insect.geo.json");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/leaf_insect_1.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/leaf_insect_2.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/leaf_insect_3.png")};
    private static final ResourceLocation ANIMATION = new ResourceLocation(CrittersAndCompanions.MODID, "animations/leaf_insect.animation.json");

    @Override
    public ResourceLocation getModelResource(LeafInsectEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(LeafInsectEntity object) {
        return TEXTURES[object.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(LeafInsectEntity animatable) {
        return ANIMATION;
    }
}
