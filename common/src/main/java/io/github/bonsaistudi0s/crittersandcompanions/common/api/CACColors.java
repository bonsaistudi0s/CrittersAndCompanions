package io.github.bonsaistudi0s.crittersandcompanions.common.api;

import net.minecraft.world.item.DyeColor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class CACColors {

    private static final Set<DyeColor> SUPPORTED_COLORS = new HashSet<>();

    public static Stream<DyeColor> supported() {
        return SUPPORTED_COLORS.stream();
    }

    public static void register(DyeColor color) {
        SUPPORTED_COLORS.add(color);
    }

}
