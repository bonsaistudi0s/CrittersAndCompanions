package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.SeaBunnyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SeaBunnyModel extends GeoModel<SeaBunnyEntity> {
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
    public void setCustomAnimations(SeaBunnyEntity entity, long uniqueID, AnimationState<SeaBunnyEntity> animationState) {
        super.setCustomAnimations(entity, uniqueID, animationState);
        CoreGeoBone mainBone = this.getAnimationProcessor().getBone("main");
        EntityModelData extraData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        mainBone.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180.0F));
        if (entity.isClimbing()) {
            mainBone.setRotX(90.0F * ((float) Math.PI / 180.0F));
        } else {
            mainBone.setRotX(0.0F);
        }
    }
}

