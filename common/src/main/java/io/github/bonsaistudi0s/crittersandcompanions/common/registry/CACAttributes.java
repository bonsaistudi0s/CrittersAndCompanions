package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class CACAttributes {

    @ExpectPlatform
    public static Attribute getSwimSpeed() {
        throw new AssertionError();
    }
}
