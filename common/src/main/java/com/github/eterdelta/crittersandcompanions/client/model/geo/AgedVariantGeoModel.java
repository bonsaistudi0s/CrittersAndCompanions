package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.BehaviourDriven;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AgeableMob;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class AgedVariantGeoModel<T extends AgeableMob & GeoAnimatable & BehaviourDriven> extends AgingVariantGeoModel<T> {

    private final ResourceLocation babyTexture;

    public AgedVariantGeoModel(ResourceLocation id) {
        super(id);
        this.babyTexture = buildFormattedTexturePath(id.withPrefix("baby_"));
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        if (entity.isBaby()) return babyTexture;
        return super.getTextureResource(entity);
    }

}
