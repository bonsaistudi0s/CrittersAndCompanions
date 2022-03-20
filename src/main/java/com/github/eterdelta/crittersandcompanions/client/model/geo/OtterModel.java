package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class OtterModel extends AnimatedGeoModel<OtterEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "geo/otter.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/otter.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(CrittersAndCompanions.MODID, "animations/otter.animation.json");

    @Override
    public ResourceLocation getModelLocation(OtterEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(OtterEntity object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(OtterEntity animatable) {
        return ANIMATION;
    }

    @Override
    public void setLivingAnimations(OtterEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone rotBone = this.getAnimationProcessor().getBone(entity.isInWater() ? "main" : "head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (!entity.isEating() && !entity.isFloating()) {
            rotBone.setRotationX(extraData.headPitch * ((float) Math.PI / 180.0F));
            rotBone.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180.0F));
        }

        if (entity.isBaby()) {
            IBone head = this.getAnimationProcessor().getBone("head");
            IBone main = this.getAnimationProcessor().getBone("main");
            head.setScaleX(1.25F);
            head.setScaleY(1.25F);
            head.setScaleZ(1.25F);
            main.setScaleX(0.5F);
            main.setScaleY(0.5F);
            main.setScaleZ(0.5F);
        }
    }
}