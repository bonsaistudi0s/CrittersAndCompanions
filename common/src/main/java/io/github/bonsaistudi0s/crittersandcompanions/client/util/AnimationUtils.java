package io.github.bonsaistudi0s.crittersandcompanions.client.util;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AnimationUtils {

    public static Vector3f calculateInverseWorldRotation(CoreGeoBone fixBone) {
        List<CoreGeoBone> parents = new ArrayList<>();
        var current = fixBone.getParent();
        while (current != null) {
            parents.add(current);
            current = current.getParent();
        }

        Collections.reverse(parents);

        var worldRot = new Quaternionf();
        for (var parent : parents) {
            var localRot = new Quaternionf()
                    .rotateX(parent.getRotX())
                    .rotateY(parent.getRotY())
                    .rotateZ(parent.getRotZ());

            worldRot.mul(localRot);
        }

        worldRot.invert();

        var invertedEulers = new Vector3f();
        worldRot.getEulerAnglesXYZ(invertedEulers);
        return invertedEulers;
    }

    public static AnimationController<?> getAnimationController(GeoAnimatable animatable, long instanceId, String controllerName) {
        return animatable.getAnimatableInstanceCache().getManagerForId(instanceId).getAnimationControllers().get(controllerName);
    }

    public static @Nullable String getCurrentAnimationName(GeoAnimatable animatable, long instanceId) {
        return getCurrentAnimationName(animatable, instanceId, "controller");
    }

    public static @Nullable String getCurrentAnimationName(GeoAnimatable animatable, long instanceId, String controllerName) {
        var controller = getAnimationController(animatable, instanceId, controllerName);
        if (controller == null) {
            return null;
        }

        var currentAnimation = controller.getCurrentAnimation();
        if (currentAnimation == null) {
            return null;
        }

        return currentAnimation.animation().name();
    }
}
