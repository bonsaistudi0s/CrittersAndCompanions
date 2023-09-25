package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.JumpingSpiderEntity;
import com.github.eterdelta.crittersandcompanions.entity.RedPandaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

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

    @Override
    public void setCustomAnimations(JumpingSpiderEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone headBone = this.getAnimationProcessor().getBone("head_rotation");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        headBone.setRotationX(extraData.headPitch * ((float) Math.PI / 180.0F));
        headBone.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180.0F));
        entity.getFactory().getOrCreateAnimationData(uniqueID).setResetSpeedInTicks(0);
    }
}
