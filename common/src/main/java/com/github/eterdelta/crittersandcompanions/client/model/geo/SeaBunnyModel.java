package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.SeaBunnyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SeaBunnyModel extends DefaultedEntityGeoModel<SeaBunnyEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "sea_bunny");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/sea_bunny_white.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/sea_bunny_blue.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/sea_bunny_yellow.png")};

    public SeaBunnyModel() {
        super(MODEL);
    }

    @Override
    public ResourceLocation getTextureResource(SeaBunnyEntity object) {
        return TEXTURES[object.getVariant()];
    }

    @Override
    public void setCustomAnimations(SeaBunnyEntity animatable, long instanceId, AnimationState<SeaBunnyEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        EntityModelData data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        var mainBone = this.getAnimationProcessor().getBone("main");

        mainBone.setRotY(data.netHeadYaw() * ((float) Math.PI / 180.0F));
        if (animatable.isClimbing()) {
            mainBone.setRotX(90.0F * ((float) Math.PI / 180.0F));
        } else {
            mainBone.setRotX(0F);
        }
    }
}

