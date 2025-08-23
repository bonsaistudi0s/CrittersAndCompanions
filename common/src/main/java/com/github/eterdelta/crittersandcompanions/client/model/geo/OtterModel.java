package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class OtterModel extends GeoModel<OtterEntity> {
    private static final ResourceLocation[] MODELS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/entity/otter.geo.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/entity/baby_otter.geo.json")};
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/otter.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/baby_otter.png")};
    private static final ResourceLocation[] ANIMATIONS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/entity/otter.animation.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/entity/baby_otter.animation.json")};

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

        // This bone rotation predicate should be done separately as it could be overlapped with the net head azimuth
        CoreGeoBone root = getAnimationProcessor().getBone("main");
        if (root != null) {
            if (!animatable.isInWater()) {
                // Hard bypasses all rotations of the core bone on land so the whole body stays in the default rotation,
                // thus preventing random pitch spikes (that basically happen when the otter is moving its head). Another
                // option is to cage the rots based on the otter state (more robust), but this impl requires less code
                root.setRotX(0);
                root.setRotY(0);
                root.setRotZ(0); // Prevents wobble on +Z coordinates (probably residual?)
            }

        }

        // Enables the yaw by default everywhere everytime, while the pitch is only computed inside water
        CoreGeoBone headOrMain = getAnimationProcessor().getBone(animatable.isInWater() ? "main" : "head");
        if (headOrMain != null && !animatable.isEating() && !animatable.isFloating()) {
            EntityModelData data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

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