package com.github.eterdelta.crittersandcompanions.platform;

import java.util.function.Supplier;

public interface RegistryHelper<T> {

    <R extends T> RegistryEntry<R> register(String id, Supplier<? extends R> creator);

}
