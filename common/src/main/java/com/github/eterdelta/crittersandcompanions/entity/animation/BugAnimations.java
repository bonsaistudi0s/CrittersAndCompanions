package com.github.eterdelta.crittersandcompanions.entity.animation;

import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.BehaviourDriven;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.Behaviours;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.DancingBehaviour;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

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
        if (state.getAnimatable() instanceof FlyingAnimal animal && animal.isFlying()) {
            return RawAnimation.begin().thenLoop("fly");
        }
        if (dancingBehaviour != null && dancingBehaviour.isDancing()) {
            return RawAnimation.begin().thenLoop(dancingBehaviour.getDanceAnimationName());
        }

        if (state.getAnimatable() instanceof TamableAnimal animal && animal.isInSittingPose()) {
            return RawAnimation.begin().thenLoop("sit");
        }

        if (state.isMoving()) {
            return RawAnimation.begin().thenLoop("walk");
        }

        return RawAnimation.begin().thenLoop("idle");
    }

}
