package com.github.eterdelta.crittersandcompanions.platform.event;

import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

@FunctionalInterface
public interface RegisterEntityRenderers {

    <T extends Entity> void accept(EntityType<T> type, EntityRendererProvider<T> provider);

    default <T extends Entity> void accept(RegistryEntry<EntityType<T>> entry, EntityRendererProvider<T> provider) {
        accept(entry.get(), provider);
    }

    default <T extends Entity & GeoAnimatable, M extends GeoModel<T>> void accept(RegistryEntry<EntityType<T>> entry, BiFunction<EntityRendererProvider.Context, M, EntityRenderer<T>> provider, Function<ResourceLocation, M> model) {
        accept(entry.get(), context -> provider.apply(context, model.apply(entry.getKey().location())));
    }

}
