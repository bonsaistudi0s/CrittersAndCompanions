package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.capability.ISilkLeashStateCapability;
import net.minecraftforge.common.util.LazyOptional;

public interface ILeashStateEntity {

    LazyOptional<ISilkLeashStateCapability> getLeashStateCache();

    void sendLeashState();
}
