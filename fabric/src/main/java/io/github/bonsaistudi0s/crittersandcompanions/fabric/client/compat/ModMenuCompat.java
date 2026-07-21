package io.github.bonsaistudi0s.crittersandcompanions.fabric.client.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;

@SuppressWarnings("unused")
public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CACCommonConfig::createConfigScreen;
    }
}
