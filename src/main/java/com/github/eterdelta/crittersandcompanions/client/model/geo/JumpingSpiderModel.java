package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.JumpingSpiderEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class JumpingSpiderModel extends GeoModel<JumpingSpiderEntity> {
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

    @Override
    public void setCustomAnimations(JumpingSpiderEntity entity, long uniqueID, AnimationState<JumpingSpiderEntity> customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        CoreGeoBone headBone = this.getAnimationProcessor().getBone("head_rotation");
        EntityModelData extraData = customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);

        headBone.setRotX(extraData.headPitch() * ((float) Math.PI / 180.0F));
        headBone.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180.0F));
//        entity.getAnimatableInstanceCache().getOrCreateAnimationData(uniqueID).setResetSpeedInTicks(0);

    }
}
