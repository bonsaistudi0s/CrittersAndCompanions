package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BehaviourDriven;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.VariantBehaviour;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class VariantGeoModel<T extends GeoAnimatable & BehaviourDriven> extends DefaultedEntityGeoModel<T> {

    private final Function<Integer, ResourceLocation> texture;

    public VariantGeoModel(ResourceLocation id) {
        super(id);
        this.texture = Util.memoize(variant -> buildFormattedTexturePath(id.withSuffix("_" + variant)));
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        return texture.apply(entity.behaviour(VariantBehaviour.class).getVariant() + 1);
    }

}
