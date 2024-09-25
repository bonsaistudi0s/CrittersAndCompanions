package com.github.eterdelta.crittersandcompanions.platform;

import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public interface RegistryEntry<T> extends Supplier<T> {

    ResourceKey<T> getKey();

}
