package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CACCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
                                                                                                  CrittersAndCompanions.MODID);
    public static final List<Supplier<? extends ItemLike>> CREATIVE_TAB_ITEMS = new ArrayList<>();
    public static final RegistryObject<CreativeModeTab> CRITTERS_AND_COMPANIONS = CREATIVE_TABS.register(
            "critters_and_companions",
            () -> CreativeModeTab
                    .builder()
                    .title(Component.translatable("itemGroup.crittersandcompanions"))
                    .icon(() -> new ItemStack(CACItems.PEARL_NECKLACE_1.get()))
                    .displayItems((params, output) -> {
                        CREATIVE_TAB_ITEMS.forEach(item -> output.accept(item.get()));
                    }).build());

    public static RegistryObject<Item> addCreativeTabItem(RegistryObject<Item> item) {
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }

}
