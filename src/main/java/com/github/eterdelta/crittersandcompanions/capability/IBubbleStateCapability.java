package com.github.eterdelta.crittersandcompanions.capability;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IBubbleStateCapability {

    boolean isActive();

    void setActive(boolean active);
}
