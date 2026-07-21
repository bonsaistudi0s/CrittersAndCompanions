package io.github.bonsaistudi0s.crittersandcompanions.common.registry.forge;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;

@SuppressWarnings("unused")
public class CACAttributesImpl {

    public static Attribute getSwimSpeed() {
        return ForgeMod.SWIM_SPEED.get();
    }
}
