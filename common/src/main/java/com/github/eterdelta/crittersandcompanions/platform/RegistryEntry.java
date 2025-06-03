package com.github.eterdelta.crittersandcompanions.platform;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;

public interface RegistryEntry<T> extends Supplier<T> {

    ResourceKey<T> getKey();

}
