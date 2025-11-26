package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.FerretEntity;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.VariantBehaviour;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FerretModel extends GeoModel<FerretEntity> {
    private static final ResourceLocation[] MODELS = new ResourceLocation[]{
            CrittersAndCompanions.createId("geo/entity/ferret.geo.json"),
            CrittersAndCompanions.createId("geo/entity/baby_ferret.geo.json")};
    private static final ResourceLocation[] ADULT_TEXTURES = new ResourceLocation[]{
            CrittersAndCompanions.createId("textures/entity/ferret_1.png"),
            CrittersAndCompanions.createId("textures/entity/ferret_2.png")};
    private static final ResourceLocation[] BABY_TEXTURES = new ResourceLocation[]{
            CrittersAndCompanions.createId("textures/entity/baby_ferret_1.png"),
            CrittersAndCompanions.createId("textures/entity/baby_ferret_2.png")};
    private static final ResourceLocation[] ANIMATIONS = new ResourceLocation[]{
            CrittersAndCompanions.createId("animations/entity/ferret.animation.json"),
            CrittersAndCompanions.createId("animations/entity/baby_ferret.animation.json")};

    @Override
    public ResourceLocation getModelResource(FerretEntity object) {
        return MODELS[object.isBaby() ? 1 : 0];
    }

    @Override
    public ResourceLocation getTextureResource(FerretEntity object) {
        var variant = object.behaviour(VariantBehaviour.class).getVariant();
        return object.isBaby() ? BABY_TEXTURES[variant] : ADULT_TEXTURES[variant];
    }

    @Override
    public ResourceLocation getAnimationResource(FerretEntity animatable) {
        return ANIMATIONS[animatable.isBaby() ? 1 : 0];
    }

    @Override
    public void setCustomAnimations(FerretEntity animatable, long instanceId, AnimationState<FerretEntity> animationState) {
        var head = getAnimationProcessor().getBone("head");
        var neck = getAnimationProcessor().getBone("head_rotation");
        var moving = animationState.isMoving();

        if (neck != null && head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            var pitch = entityData.headPitch();
            neck.setRotX(Math.min(20, pitch) * Mth.DEG_TO_RAD);
            if (pitch > 20 && !moving) head.setRotX((pitch - 30) * Mth.DEG_TO_RAD);
            neck.setRotZ(entityData.netHeadYaw() * Mth.DEG_TO_RAD * -0.5F);
        }
    }

}
