package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.DumboOctopusEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DumboOctopusModel extends AnimatedGeoModel<DumboOctopusEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "geo/dumbo_octopus.geo.json");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/dumbo_octopus_1.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/dumbo_octopus_2.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/dumbo_octopus_3.png")};
    private static final ResourceLocation ANIMATION = new ResourceLocation(CrittersAndCompanions.MODID, "animations/dumbo_octopus.animation.json");

    @Override
    public ResourceLocation getModelLocation(DumboOctopusEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(DumboOctopusEntity object) {
        return TEXTURES[object.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DumboOctopusEntity animatable) {
        return ANIMATION;
    }
}
