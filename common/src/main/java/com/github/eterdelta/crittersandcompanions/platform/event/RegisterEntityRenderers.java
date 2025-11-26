package com.github.eterdelta.crittersandcompanions.platform.event;

import com.github.eterdelta.crittersandcompanions.client.model.geo.VariantGeoModel;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@FunctionalInterface
public interface RegisterEntityRenderers {

    <T extends Entity> void accept(EntityType<T> type, EntityRendererProvider<T> provider);

    default <T extends Entity> void accept(RegistryEntry<EntityType<T>> entry, EntityRendererProvider<T> provider) {
        accept(entry.get(), provider);
    }

    default <T extends Mob & GeoAnimatable> void variant(RegistryEntry<EntityType<T>> entry) {
        accept(entry, context -> new GeoEntityRenderer<>(context, new VariantGeoModel<>(entry.getKey().location())));
    }

    default <T extends Mob & GeoAnimatable> void simple(RegistryEntry<EntityType<T>> entry) {
        accept(entry, context -> new GeoEntityRenderer<>(context, new DefaultedEntityGeoModel<>(entry.getKey().location())));
    }

}
