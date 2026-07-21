package io.github.bonsaistudi0s.crittersandcompanions.common.registry.forge;

import io.github.bonsaistudi0s.crittersandcompanions.common.item.forge.AcornHatItemImpl;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class CACItemsImpl {

    public static Supplier<? extends Item> getAcornHatItem(Item.Properties properties) {
        return () -> new AcornHatItemImpl(properties);
    }
}
