package com.github.eterdelta.crittersandcompanions.platform;

import com.github.eterdelta.crittersandcompanions.platform.service.IEvents;
import com.github.eterdelta.crittersandcompanions.platform.service.INetwork;
import com.github.eterdelta.crittersandcompanions.platform.service.IPlatformHelper;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IEvents EVENTS = load(IEvents.class);
    public static final INetwork NETWORK = load(INetwork.class);

    private static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}