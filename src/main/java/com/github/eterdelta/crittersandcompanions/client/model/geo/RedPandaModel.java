package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.RedPandaEntity;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class RedPandaModel extends AnimatedGeoModel<RedPandaEntity> {
    private static final ResourceLocation[] MODELS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/red_panda.geo.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/baby_red_panda.geo.json")};
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/red_panda.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/red_panda_sleeping.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/baby_red_panda.png")};
    private static final ResourceLocation[] ANIMATIONS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/red_panda.animation.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/baby_red_panda.animation.json")};

    @Override
    public ResourceLocation getModelLocation(RedPandaEntity object) {
        return MODELS[object.isBaby() ? 1 : 0];
    }

    @Override
    public ResourceLocation getTextureLocation(RedPandaEntity object) {
        return TEXTURES[object.isBaby() ? 2 : object.isSleeping() ? 1 : 0];
    }

    @Override
    public ResourceLocation getAnimationFileLocation(RedPandaEntity animatable) {
        return ANIMATIONS[animatable.isBaby() ? 1 : 0];
    }

    @Override
    public void setLivingAnimations(RedPandaEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone headBone = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        if (!entity.isSleeping()) {
            if (!entity.isAlert()) {
                headBone.setRotationX(extraData.headPitch * ((float) Math.PI / 180.0F));
            }
            headBone.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180.0F));
        }
    }
}
