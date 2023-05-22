package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import com.github.eterdelta.crittersandcompanions.item.DragonflyArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DragonflyModel extends AnimatedGeoModel<DragonflyEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "geo/dragonfly.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/dragonfly.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(CrittersAndCompanions.MODID, "animations/dragonfly.animation.json");

    @Override
    public ResourceLocation getModelResource(DragonflyEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(DragonflyEntity object) {
        return object.getArmor().isEmpty() ? TEXTURE : ((DragonflyArmorItem) object.getArmor().getItem()).getTexture();
    }

    @Override
    public ResourceLocation getAnimationResource(DragonflyEntity animatable) {
        return ANIMATION;
    }
}
