package com.github.eterdelta.crittersandcompanions.capability;

import com.github.eterdelta.crittersandcompanions.entity.GrapplingHookEntity;

public class GrapplingState implements IGrapplingStateCapability {
    private GrapplingHookEntity hook;

    @Override
    public GrapplingHookEntity getHook() {
        return this.hook;
    }

    @Override
    public void setHook(GrapplingHookEntity hook) {
        this.hook = hook;
    }
}
