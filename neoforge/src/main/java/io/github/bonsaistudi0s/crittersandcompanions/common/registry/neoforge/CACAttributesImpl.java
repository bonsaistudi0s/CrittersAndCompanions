package io.github.bonsaistudi0s.crittersandcompanions.common.registry.neoforge;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.NeoForgeMod;

@SuppressWarnings("unused")
public class CACAttributesImpl {

    public static Holder<Attribute> getSwimSpeed() {
        return NeoForgeMod.SWIM_SPEED;
    }
}
