package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class OtterModel extends GeoModel<OtterEntity> {
    private static final ResourceLocation[] MODELS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/otter.geo.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/baby_otter.geo.json")};
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/otter.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/baby_otter.png")};
    private static final ResourceLocation[] ANIMATIONS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/otter.animation.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/baby_otter.animation.json")};

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
    public void setCustomAnimations(OtterEntity entity, long uniqueID, AnimationState<OtterEntity> customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        CoreGeoBone rotBone = this.getAnimationProcessor().getBone(entity.isInWater() ? "main" : "head");

        EntityModelData extraData = customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
        if (!entity.isEating() && !entity.isFloating()) {
            rotBone.setRotX(extraData.headPitch() * ((float) Math.PI / 180.0F));
            rotBone.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180.0F));
        }
        //entity.getAnimatableInstanceCache().getManagerForId(uniqueID).setData(DataTickets.);
    }
}