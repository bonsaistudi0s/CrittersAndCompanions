package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.entity.SnailEntity;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.ClimbingBehaviour;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;

public class SnailModel extends AgedVariantGeoModel<SnailEntity> {

    public SnailModel(ResourceLocation id) {
        super(id);
    }

    @Override
    public void setCustomAnimations(SnailEntity animatable, long instanceId, AnimationState<SnailEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var mainBone = this.getAnimationProcessor().getBone("main");
        
        if (animatable.behaviour(ClimbingBehaviour.class).isClimbing()) {
            mainBone.setRotX(90.0F * ((float) Math.PI / 180.0F));
        } else {
            mainBone.setRotX(0F);
        }
    }
}
