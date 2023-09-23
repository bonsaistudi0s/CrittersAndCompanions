package com.github.eterdelta.crittersandcompanions.capability;

import com.github.eterdelta.crittersandcompanions.entity.GrapplingHookEntity;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IGrapplingStateCapability {

    GrapplingHookEntity getHook();

    void setHook(GrapplingHookEntity hook);
}
