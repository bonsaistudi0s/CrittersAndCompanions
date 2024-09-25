package com.github.eterdelta.crittersandcompanions.platform;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForgeRegistryHelper<T> implements RegistryHelper<T> {

    protected final DeferredRegister<T> deferred;

    public ForgeRegistryHelper(ResourceKey<Registry<T>> key, String modid) {
        this.deferred = DeferredRegister.create(key, modid);

        var modContext = FMLJavaModLoadingContext.get();
        if(modContext == null) {
            throw new IllegalStateException("created registry helper too late: %s for %s".formatted(key, modid));
        }

        deferred.register(modContext.getModEventBus());
    }

    @Override
    public <R extends T> RegistryEntry<R> register(String id, Supplier<? extends R> creator) {
        var value = deferred.register(id, creator);
        return new RegistryEntry<>() {
            @Override
            public R get() {
                return value.get();
            }

            @SuppressWarnings("unchecked")
            @Override
            public ResourceKey<R> getKey() {
                return (ResourceKey<R>) value.getKey();
            }
        };
    }

    public static class ItemHelper extends ForgeRegistryHelper<Item> {

        public ItemHelper(String modid) {
            super(Registries.ITEM, modid);

            IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
            modBus.addListener((BuildCreativeModeTabContentsEvent event) -> {
                if (event.getTab() != CrittersAndCompanions.CREATIVE_TAB.get()) return;

                deferred.getEntries().forEach(it -> event.accept(new ItemStack(it.get())));
            });
        }

    }

}
