package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.RedPandaEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class RedPandaModel extends AgingGeoModel<RedPandaEntity> {

    private final ResourceLocation sleepingTexture;

    public RedPandaModel(ResourceLocation id) {
        super(id);
        this.sleepingTexture = buildFormattedTexturePath(id.withSuffix("_sleeping"));
    }

    @Override
    public ResourceLocation getTextureResource(RedPandaEntity object) {
        if(!object.isBaby() && object.isSleeping()) return  sleepingTexture;
        return super.getTextureResource(object);
    }

    @Override
    public void setCustomAnimations(RedPandaEntity animatable, long instanceId, AnimationState<RedPandaEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        EntityModelData data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        var neck = this.getAnimationProcessor().getBone("head");

        if (!animatable.isSleeping() && !animatable.isInSittingPose()) {
            if (!animatable.isAlert()) {
                neck.setRotX(data.headPitch() * Mth.DEG_TO_RAD);
            }
            neck.setRotY(data.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
