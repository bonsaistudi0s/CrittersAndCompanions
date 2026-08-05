package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.OtterEntity;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;

public class OtterModel extends AgingGeoModel<OtterEntity> {

    public OtterModel() {
        super(CrittersAndCompanions.createId("otter"));
    }

    @Override
    public void setCustomAnimations(OtterEntity animatable, long instanceId, AnimationState<OtterEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        // This bone rotation predicate should be done separately as it could be overlapped with the net head azimuth
        var root = getAnimationProcessor().getBone("main");
        if (root != null) {
            if (!animatable.isInWater() && !animatable.isFloating()) {
                // Hard bypasses all rotations of the core bone on land so the whole body stays in the default rotation,
                // thus preventing random pitch spikes (that basically happen when the otter is moving its head). Another
                // option is to cage the rots based on the otter state (more robust), but this impl requires less code
                root.setRotX(0);
                root.setRotY(0);
                root.setRotZ(0); // Prevents wobble on +Z coordinates (probably residual?)
            }

        }

        // Enables the yaw by default everywhere everytime, while the pitch is only computed inside water
        var headOrMain = getAnimationProcessor().getBone(animatable.isInWater() ? "main" : "head");
        if (headOrMain != null && !animatable.isEating() && !animatable.isFloating()) {
            var data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            // Yaw is clamped within a minimal side rotation to avoid jittery which could lead to the head bone
            // to tweak a bit
            float yaw = Mth.clamp(data.netHeadYaw(), -35.0F, 35.0F);
            // Pitch is only computed inside water as it's the only condition where the otter should rotate
            float pitch = animatable.isInWater() ? Mth.clamp(data.headPitch(), -90.0F, 90.0F) : 0.0F;

            // Damping
            if (!animatable.isInWater() && animationState.isMoving()) yaw *= 0.6F;

            headOrMain.setRotY(yaw * Mth.DEG_TO_RAD);
            headOrMain.setRotX(pitch * Mth.DEG_TO_RAD);
        }

    }

}