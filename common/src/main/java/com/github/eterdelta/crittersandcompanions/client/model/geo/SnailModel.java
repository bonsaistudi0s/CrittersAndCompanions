package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.SnailEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;

public class SnailModel extends AgedVariantGeoModel<SnailEntity> {

    public SnailModel(ResourceLocation id) {
        super(id);
    }

    @Override
    public ResourceLocation getTextureResource(SnailEntity entity) {
        if (entity.isGaryVariant()) {
            if (entity.isBaby()) {
                return CrittersAndCompanions.createId("textures/entity/baby_snail_gary.png");
            } else {
                return CrittersAndCompanions.createId("textures/entity/snail_gary.png");
            }
        }

        return super.getTextureResource(entity);
    }

    @Override
    public void setCustomAnimations(SnailEntity animatable, long instanceId,
                                    AnimationState<SnailEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var mainBone = this.getAnimationProcessor().getBone("main");

        if (animatable.onClimbable() && !animatable.onGround()) {
            mainBone.setRotX(90.0F * ((float) Math.PI / 180.0F));
            mainBone.setPosY(animatable.isBaby() ? 4.0F : 6.0F);
        } else {
            mainBone.setRotX(0F);
            mainBone.setPosY(0F);
        }
    }
}
