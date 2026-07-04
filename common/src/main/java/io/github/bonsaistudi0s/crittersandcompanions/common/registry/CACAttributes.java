package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class CACAttributes {

    @ExpectPlatform
    public static Holder<Attribute> getSwimSpeed() {
        throw new AssertionError();
    }
}
