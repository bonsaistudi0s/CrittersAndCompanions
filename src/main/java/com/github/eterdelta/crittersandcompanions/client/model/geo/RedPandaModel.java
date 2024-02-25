package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.RedPandaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class RedPandaModel extends GeoModel<RedPandaEntity> {
    private static final ResourceLocation[] MODELS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/red_panda.geo.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/baby_red_panda.geo.json")};
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/red_panda.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/red_panda_sleeping.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/baby_red_panda.png")};
    private static final ResourceLocation[] ANIMATIONS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/red_panda.animation.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/baby_red_panda.animation.json")};

    @Override
    public ResourceLocation getModelResource(RedPandaEntity object) {
        return MODELS[object.isBaby() ? 1 : 0];
    }

    @Override
    public ResourceLocation getTextureResource(RedPandaEntity object) {
        return TEXTURES[object.isBaby() ? 2 : object.isSleeping() ? 1 : 0];
    }

    @Override
    public ResourceLocation getAnimationResource(RedPandaEntity animatable) {
        return ANIMATIONS[animatable.isBaby() ? 1 : 0];
    }

    @Override
    public void setCustomAnimations(RedPandaEntity entity, long uniqueID, AnimationState<RedPandaEntity> customPredicate) {

        super.setCustomAnimations(entity, uniqueID, customPredicate);
        CoreGeoBone headBone = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);

        if (!entity.isSleeping() && !entity.isInSittingPose()) {
            if (!entity.isAlert()) {
                headBone.setRotX(extraData.headPitch() * ((float) Math.PI / 180.0F));
            }
            headBone.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180.0F));
        }
//        entity.getAnimatableInstanceCache().getManagerForId(uniqueID).setData(DataTickets.);
    }
}
