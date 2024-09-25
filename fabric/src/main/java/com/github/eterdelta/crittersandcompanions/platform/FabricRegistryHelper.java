package com.github.eterdelta.crittersandcompanions.platform;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class FabricRegistryHelper<T> implements RegistryHelper<T> {

    @SuppressWarnings("unchecked")
    private static <T> Registry<T> getRegistryOrThrow(ResourceKey<Registry<T>> registryKey) {
        var registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
        if (registry == null) {
            throw new IllegalArgumentException("Could not locate registry for %s".formatted(registryKey.location()));
        }
        return registry;
    }

    private final String modid;
    private final ResourceKey<Registry<T>> registryKey;
    private final Registry<T> registry;

    public FabricRegistryHelper(ResourceKey<Registry<T>> registryKey, String modid) {
        this.modid = modid;
        this.registryKey = registryKey;
        this.registry = getRegistryOrThrow(registryKey);
    }

    @Override
    public <R extends T> RegistryEntry<R> register(String id, Supplier<? extends R> creator) {
        var key = ResourceKey.create(registryKey, new ResourceLocation(modid, id));
        var registered = Registry.register(registry, key, creator.get());
        return new RegistryEntry<>() {
            @Override
            public R get() {
                return registered;
            }

            @SuppressWarnings("unchecked")
            @Override
            public ResourceKey<R> getKey() {
                return (ResourceKey<R>) key;
            }
        };
    }

    public static class ItemHelper extends FabricRegistryHelper<Item> {

        public ItemHelper(String modid) {
            super(Registries.ITEM, modid);
        }

        @Override
        public <R extends Item> RegistryEntry<R> register(String id, Supplier<? extends R> creator) {
            RegistryEntry<R> item = super.register(id, creator);

            ItemGroupEvents.modifyEntriesEvent(CrittersAndCompanions.CREATIVE_TAB.getKey())
                    .register(entries -> entries.accept(new ItemStack(item.get())));

            return item;
        }
    }
}
