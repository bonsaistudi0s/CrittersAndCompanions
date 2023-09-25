package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.KoiFishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KoiFishModel extends AnimatedGeoModel<KoiFishEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "geo/koi_fish.geo.json");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_1.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_2.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_3.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_4.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_5.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_6.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_7.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_8.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_9.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_10.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_11.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_12.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_13.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_14.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_15.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_16.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_17.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_18.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_19.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_20.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/koi_fish_21.png")};
    private static final ResourceLocation ANIMATION = new ResourceLocation(CrittersAndCompanions.MODID, "animations/koi_fish.animation.json");

    @Override
    public ResourceLocation getModelResource(KoiFishEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(KoiFishEntity object) {
        return TEXTURES[object.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(KoiFishEntity animatable) {
        return ANIMATION;
    }
}
