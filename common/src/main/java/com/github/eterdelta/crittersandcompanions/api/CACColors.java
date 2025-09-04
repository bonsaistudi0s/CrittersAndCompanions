package com.github.eterdelta.crittersandcompanions.api;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.world.item.DyeColor;

public class CACColors {

    private static final Set<DyeColor> SUPPORTED_COLORS = new HashSet<>();

    public static Stream<DyeColor> supported() {
        return SUPPORTED_COLORS.stream();
    }

    public static void register(DyeColor color) {
        SUPPORTED_COLORS.add(color);
    }

}
