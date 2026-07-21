package io.github.bonsaistudi0s.crittersandcompanions.common.registry.fabric;

import io.github.fabricators_of_create.porting_lib.attributes.PortingLibAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;

@SuppressWarnings("unused")
public class CACAttributesImpl {

    public static Attribute getSwimSpeed() {
        return PortingLibAttributes.SWIM_SPEED;
    }
}
