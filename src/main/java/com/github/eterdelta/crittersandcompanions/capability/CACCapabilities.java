package com.github.eterdelta.crittersandcompanions.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CACCapabilities {
    public static final Capability<IBubbleStateCapability> BUBBLE_STATE = CapabilityManager.get(new CapabilityToken<>(){});
}
