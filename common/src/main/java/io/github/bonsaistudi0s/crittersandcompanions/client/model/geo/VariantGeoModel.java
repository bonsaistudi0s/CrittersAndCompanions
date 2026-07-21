package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BehaviourDriven;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.VariantBehaviour;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.function.Function;

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
