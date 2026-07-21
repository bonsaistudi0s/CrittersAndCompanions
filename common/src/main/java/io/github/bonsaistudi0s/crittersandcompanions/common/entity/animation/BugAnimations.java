package io.github.bonsaistudi0s.crittersandcompanions.common.entity.animation;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.FlyingAnimal;

import org.jetbrains.annotations.Nullable;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BehaviourDriven;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviours;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.DancingBehaviour;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class BugAnimations<T extends GeoAnimatable & BehaviourDriven> implements AnimationController.AnimationStateHandler<T> {

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("fly");
    private static final RawAnimation SIT_ANIM = RawAnimation.begin().thenLoop("sit");

    private static final int TRANSITION_TICK_TIME = 4;

    @Nullable
    private final DancingBehaviour dancingBehaviour;

    public BugAnimations(Behaviours behaviours) {
        this.dancingBehaviour = behaviours.optional(DancingBehaviour.class).orElse(null);
    }

    public static <T extends GeoAnimatable & BehaviourDriven> AnimationController<T> createController(T animatable) {
        return new AnimationController<>(animatable, "controller", TRANSITION_TICK_TIME, new BugAnimations<>(animatable.getBehaviours()));
    }

    @Override
    public PlayState handle(AnimationState<T> state) {
        var custom = getCustomAnimation(state);
        if (custom != null) {
            state.getController().setAnimation(custom);
            return PlayState.CONTINUE;
        }

        state.getController().setAnimation(createAnimation(state));
        return PlayState.CONTINUE;
    }

    protected @Nullable RawAnimation getCustomAnimation(AnimationState<T> state) {
        return null;
    }

    private RawAnimation createAnimation(AnimationState<T> state) {
        var controller = state.getController();

        if (state.getAnimatable() instanceof FlyingAnimal animal && animal.isFlying()) {
            controller.transitionLength(TRANSITION_TICK_TIME);
            return FLY_ANIM;
        }

        if (dancingBehaviour != null && dancingBehaviour.isDancing()) {
            controller.transitionLength(TRANSITION_TICK_TIME);
            return RawAnimation.begin().thenLoop(dancingBehaviour.getDanceAnimationName());
        }

        if (state.getAnimatable() instanceof TamableAnimal animal && animal.isInSittingPose()) {
            controller.transitionLength(0);
            return SIT_ANIM;
        }

        if (state.isMoving()) {
            controller.transitionLength(TRANSITION_TICK_TIME);
            return WALK_ANIM;
        }

        if (state.isCurrentAnimation(SIT_ANIM)) {
            controller.transitionLength(0);
        } else {
            controller.transitionLength(TRANSITION_TICK_TIME);
        }

        return IDLE_ANIM;
    }
}
