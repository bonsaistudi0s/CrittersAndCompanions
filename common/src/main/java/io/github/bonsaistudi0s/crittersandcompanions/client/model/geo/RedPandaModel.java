package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import io.github.bonsaistudi0s.crittersandcompanions.client.util.AnimationUtils;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.RedPandaEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;

public class RedPandaModel extends AgingGeoModel<RedPandaEntity> {

    private final ResourceLocation sleepingTexture;

    public RedPandaModel(ResourceLocation id) {
        super(id);
        this.sleepingTexture = buildFormattedTexturePath(id.withSuffix("_sleeping"));
    }

    @Override
    public ResourceLocation getTextureResource(RedPandaEntity object) {
        if (!object.isBaby() && object.isSleeping()) return sleepingTexture;
        return super.getTextureResource(object);
    }

    @Override
    public void setCustomAnimations(RedPandaEntity animatable, long instanceId, AnimationState<RedPandaEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var shouldOverrideAnimationHeadRotation = !animatable.isSleeping();
        if (shouldOverrideAnimationHeadRotation) {
            var data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            var head = this.getAnimationProcessor().getBone("head");
            var headRotation = this.getAnimationProcessor().getBone("head_rotation");
            var headAxisAlign = this.getAnimationProcessor().getBone("head_axis_align");

            if (data != null && head != null && headRotation != null && headAxisAlign != null) {
                // rotation values for head_axis_align to make head_rotation have a world-space rotation of 0, 0, 0
                var axisAlign = AnimationUtils.calculateInverseWorldRotation(headAxisAlign);
                headAxisAlign.setRotX(axisAlign.x);
                headAxisAlign.setRotY(axisAlign.y);
                headAxisAlign.setRotZ(axisAlign.z);

                // counteract animation compensation on head for parent bone rotations
                head.setRotX(head.getRotX() - axisAlign.x);
                head.setRotY(head.getRotY() - axisAlign.y);
                head.setRotZ(head.getRotZ() - axisAlign.z);

                headRotation.setRotX(data.headPitch() * Mth.DEG_TO_RAD);
                headRotation.setRotY(data.netHeadYaw() * Mth.DEG_TO_RAD);
            }
        }
    }
}
