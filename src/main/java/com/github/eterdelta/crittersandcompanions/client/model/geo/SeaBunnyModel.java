package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.SeaBunnyEntity;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class SeaBunnyModel extends AnimatedGeoModel<SeaBunnyEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "geo/sea_bunny.geo.json");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/sea_bunny_white.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/sea_bunny_blue.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/sea_bunny_yellow.png")};
    private static final ResourceLocation ANIMATION = new ResourceLocation(CrittersAndCompanions.MODID, "animations/sea_bunny.animation.json");

    @Override
    public ResourceLocation getModelResource(SeaBunnyEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SeaBunnyEntity object) {
        return TEXTURES[object.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(SeaBunnyEntity animatable) {
        return ANIMATION;
    }

    @Override
    public void setCustomAnimations(SeaBunnyEntity entity, int uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone mainBone = this.getAnimationProcessor().getBone("main");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        mainBone.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180.0F));
        if (entity.isClimbing()) {
            mainBone.setRotationX(90.0F * ((float) Math.PI / 180.0F));
        }
    }
}

