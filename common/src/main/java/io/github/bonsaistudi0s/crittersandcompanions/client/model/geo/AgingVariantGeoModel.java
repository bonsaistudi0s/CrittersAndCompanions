package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AgeableMob;

import java.util.function.BiFunction;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BehaviourDriven;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.VariantBehaviour;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class AgingVariantGeoModel<T extends AgeableMob & GeoAnimatable & BehaviourDriven> extends AgingGeoModel<T> {

    private final BiFunction<Integer, Boolean, ResourceLocation> texture;

    public AgingVariantGeoModel(ResourceLocation id) {
        super(id);
        this.texture = Util.memoize((variant, baby) -> {
            var realId = baby ? id.withPrefix("baby_") : id;
            return buildFormattedTexturePath(realId.withSuffix("_" + variant));
        });
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        return texture.apply(entity.behaviour(VariantBehaviour.class).getVariant() + 1, entity.isBaby());
    }

}
