package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class OtterModel extends GeoModel<OtterEntity> {
    private static final ResourceLocation[] MODELS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/entity/otter.geo.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/entity/baby_otter.geo.json")};
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/otter.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/baby_otter.png")};
    private static final ResourceLocation[] ANIMATIONS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/entity/otter.animation.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/entity/baby_otter.animation.json")};

    @Override
    public ResourceLocation getModelResource(OtterEntity object) {
        return MODELS[object.isBaby() ? 1 : 0];
    }

    @Override
    public ResourceLocation getTextureResource(OtterEntity object) {
        return TEXTURES[object.isBaby() ? 1 : 0];
    }

    @Override
    public ResourceLocation getAnimationResource(OtterEntity animatable) {
        return ANIMATIONS[animatable.isBaby() ? 1 : 0];
    }

    @Override
    public void setCustomAnimations(OtterEntity animatable, long instanceId, AnimationState<OtterEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = getAnimationProcessor().getBone(animatable.isInWater() ? "main" : "head");

        if (head != null && !animatable.isEating() && !animatable.isFloating()) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}