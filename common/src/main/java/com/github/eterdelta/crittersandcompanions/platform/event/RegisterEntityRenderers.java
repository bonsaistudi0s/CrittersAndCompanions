package com.github.eterdelta.crittersandcompanions.platform.event;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

@FunctionalInterface
public interface RegisterEntityRenderers {

    <T extends Entity> void accept(EntityType<T> type, EntityRendererProvider<T> provider);

}
