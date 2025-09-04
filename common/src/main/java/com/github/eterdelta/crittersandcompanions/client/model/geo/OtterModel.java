package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class OtterModel extends GeoModel<OtterEntity> {
    private static final ResourceLocation[] MODELS = new ResourceLocation[]{
            CrittersAndCompanions.createId("geo/entity/otter.geo.json"),
            CrittersAndCompanions.createId("geo/entity/baby_otter.geo.json")};
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            CrittersAndCompanions.createId("textures/entity/otter.png"),
            CrittersAndCompanions.createId("textures/entity/baby_otter.png")};
    private static final ResourceLocation[] ANIMATIONS = new ResourceLocation[]{
            CrittersAndCompanions.createId("animations/entity/otter.animation.json"),
            CrittersAndCompanions.createId("animations/entity/baby_otter.animation.json")};

    @Override
    public ResourceLocation getModelResource(OtterEntity object) {
        return MODELS[object.isBaby() ? 1 : 0];
    }

    @Override
    public ResourceLocation getTextureResource(OtterEntity object) {
        return TEXTURES[object.isBaby() ? 1 : 0];
    }

    @Override
    public ResourceLocation getAnimationResource(OtterEntity animatable) {
        return ANIMATIONS[animatable.isBaby() ? 1 : 0];
    }

    @Override
    public void setCustomAnimations(OtterEntity animatable, long instanceId, AnimationState<OtterEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        var head = getAnimationProcessor().getBone("head");
        var body = getAnimationProcessor().getBone("main");

        if (head == null || body == null) return;

        var rotating = animatable.isInWater() ? body : head;

        if (!animatable.isInWater()) {
            body.setRotX(0);
        }

        if (!animatable.isEating() && !animatable.isFloating()) {
            var entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            rotating.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            rotating.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}