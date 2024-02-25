package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.FerretEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class FerretModel extends GeoModel<FerretEntity> {
    private static final ResourceLocation[] MODELS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/ferret.geo.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/baby_ferret.geo.json")};
    private static final ResourceLocation[] ADULT_TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/ferret_1.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/ferret_2.png")};
    private static final ResourceLocation[] BABY_TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/baby_ferret_1.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/baby_ferret_2.png")};
    private static final ResourceLocation[] ANIMATIONS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/ferret.animation.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/baby_ferret.animation.json")};

    @Override
    public ResourceLocation getModelResource(FerretEntity object) {
        return MODELS[object.isBaby() ? 1 : 0];
    }

    @Override
    public ResourceLocation getTextureResource(FerretEntity object) {
        return object.isBaby() ? BABY_TEXTURES[object.getVariant()] : ADULT_TEXTURES[object.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(FerretEntity animatable) {
        return ANIMATIONS[animatable.isBaby() ? 1 : 0];
    }

    @Override
    public void setCustomAnimations(FerretEntity animatable, long instanceId, AnimationState<FerretEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }

}
