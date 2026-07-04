package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AgeableMob;

import java.util.function.Function;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BehaviourDriven;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AgingGeoModel<T extends AgeableMob & GeoAnimatable & BehaviourDriven> extends DefaultedEntityGeoModel<T> {

    public static ResourceLocation[] createResources(ResourceLocation id, Function<ResourceLocation, ResourceLocation> factory) {
        return new ResourceLocation[]{
                factory.apply(id),
                factory.apply(id.withPrefix("baby_"))
        };
    }

    private final ResourceLocation[] textures;
    private final ResourceLocation[] animations;
    private final ResourceLocation[] models;

    public AgingGeoModel(ResourceLocation id) {
        super(id);
        this.textures = createResources(id, this::buildFormattedTexturePath);
        this.animations = createResources(id, this::buildFormattedAnimationPath);
        this.models = createResources(id, this::buildFormattedModelPath);
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        return entity.isBaby() ? textures[1] : textures[0];
    }

    @Override
    public ResourceLocation getModelResource(T entity) {
        return entity.isBaby() ? models[1] : models[0];
    }

    @Override
    public ResourceLocation getAnimationResource(T entity) {
        return entity.isBaby() ? animations[1] : animations[0];
    }
}
