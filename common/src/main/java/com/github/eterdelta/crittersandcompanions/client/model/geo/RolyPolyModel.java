package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.entity.RolyPolyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;

public class RolyPolyModel extends AgedVariantGeoModel<RolyPolyEntity> {

    public RolyPolyModel(ResourceLocation id) {
        super(id);
    }

    @Override
    public void setCustomAnimations(RolyPolyEntity animatable, long instanceId, AnimationState<RolyPolyEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var controller = animationState.getController();
        if (controller == null) {
            return;
        }

        var currentAnimation = controller.getCurrentAnimation();
        var animName = currentAnimation != null ? currentAnimation.animation().name() : "";

        switch (animName) {
            case "walk", "dance_2", "flipped" -> toggleLegs(true);
            default -> toggleLegs(false);
        }
    }

    private void toggleLegs(boolean useAnimatedLegs) {
        var leftLegsStill = getAnimationProcessor().getBone("left_legs");
        var leftLegsAnimated = getAnimationProcessor().getBone("left_legs_walk");
        var rightLegsStill = getAnimationProcessor().getBone("right_legs");
        var rightLegsAnimated = getAnimationProcessor().getBone("right_legs_walk");

        leftLegsStill.setHidden(useAnimatedLegs);
        leftLegsAnimated.setHidden(!useAnimatedLegs);
        rightLegsStill.setHidden(useAnimatedLegs);
        rightLegsAnimated.setHidden(!useAnimatedLegs);
    }
}
