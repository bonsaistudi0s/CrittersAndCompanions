package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.entity.RollypollyEntity;
import net.minecraft.resources.ResourceLocation;

public class RollyPollyModel extends AgingVariantGeoModel<RollypollyEntity> {

    private final ResourceLocation babyTexture;

    public RollyPollyModel(ResourceLocation id) {
        super(id);
        this.babyTexture = buildFormattedTexturePath(id.withPrefix("baby_"));
    }

    @Override
    public ResourceLocation getTextureResource(RollypollyEntity entity) {
        if (entity.isBaby()) return babyTexture;
        return super.getTextureResource(entity);
    }

}
