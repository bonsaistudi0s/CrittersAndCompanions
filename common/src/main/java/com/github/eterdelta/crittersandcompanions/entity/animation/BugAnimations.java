package com.github.eterdelta.crittersandcompanions.entity.animation;

import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.BehaviourDriven;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.Behaviours;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.DancingBehaviour;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import javax.annotation.Nullable;

public class BugAnimations<T extends GeoAnimatable & BehaviourDriven> implements AnimationController.AnimationStateHandler<T> {

    @Nullable
    private final DancingBehaviour dancingBehaviour;

    public BugAnimations(Behaviours behaviours) {
        this.dancingBehaviour = behaviours.optional(DancingBehaviour.class).orElse(null);
    }

    public static <T extends GeoAnimatable & BehaviourDriven> AnimationController<T> createController(T animatable) {
        return new AnimationController<>(animatable, "controller", 4, new BugAnimations<>(animatable.getBehaviours()));
    }

    @Override
    public PlayState handle(AnimationState<T> state) {
        state.getController().setAnimation(createAnimation(state));
        return PlayState.CONTINUE;
    }

    private RawAnimation createAnimation(AnimationState<T> state) {
        if (dancingBehaviour != null && dancingBehaviour.isDancing()) {
            return RawAnimation.begin().thenLoop("dance");
        } else if (state.isMoving()) {
            return RawAnimation.begin().thenLoop("walk");
        } else {
            return RawAnimation.begin().thenLoop("idle");
        }
    }

}
