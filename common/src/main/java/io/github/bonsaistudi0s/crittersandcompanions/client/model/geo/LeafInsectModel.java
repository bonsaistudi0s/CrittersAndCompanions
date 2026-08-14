package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import net.minecraft.util.Mth;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.LeafInsectEntity;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;

public class LeafInsectModel extends AgingVariantGeoModel<LeafInsectEntity> {

    public LeafInsectModel() {
        super(CrittersAndCompanions.createId("leaf_insect"));
    }

    @Override
    public void setCustomAnimations(LeafInsectEntity animatable, long instanceId, AnimationState<LeafInsectEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var headRotation = getAnimationProcessor().getBone("head_rotation");

        if (headRotation != null) {
            var entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            var proceduralPitch = entityData.headPitch() * Mth.DEG_TO_RAD;
            var proceduralYaw = entityData.netHeadYaw() * Mth.DEG_TO_RAD;

            var partialTick = animationState.getPartialTick();
            var lerpedWeight = Mth.lerp(partialTick, animatable.lookBlendWeightO, animatable.lookBlendWeight);

            if (lerpedWeight < 0.99f) {
                var animationPitch = headRotation.getRotX();
                var animationYaw = headRotation.getRotY();

                headRotation.setRotX(Mth.lerp(lerpedWeight, animationPitch, proceduralPitch));
                headRotation.setRotY(Mth.lerp(lerpedWeight, animationYaw, proceduralYaw));
            } else {
                headRotation.setRotX(proceduralPitch);
                headRotation.setRotY(proceduralYaw);
            }
        }
    }
}
