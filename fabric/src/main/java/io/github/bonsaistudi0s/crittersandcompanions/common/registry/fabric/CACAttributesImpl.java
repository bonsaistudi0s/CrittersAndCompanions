package io.github.bonsaistudi0s.crittersandcompanions.common.registry.fabric;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;

import io.github.fabricators_of_create.porting_lib.attributes.PortingLibAttributes;

@SuppressWarnings("unused")
public class CACAttributesImpl {

    public static Holder<Attribute> getSwimSpeed() {
        return PortingLibAttributes.SWIM_SPEED;
    }
}
