package com.github.eterdelta.crittersandcompanions.capability;

public class BubbleState implements IBubbleStateCapability {
    private boolean bubbleActive;

    @Override
    public boolean isActive() {
        return this.bubbleActive;
    }

    @Override
    public void setActive(boolean active) {
        this.bubbleActive = active;
    }
}
