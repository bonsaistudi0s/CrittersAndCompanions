package com.github.eterdelta.crittersandcompanions.platform;

import com.github.eterdelta.crittersandcompanions.config.CACCommonConfig;
import com.github.eterdelta.crittersandcompanions.platform.service.IConfigs;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class ForgeConfigs implements IConfigs {

    private static final Pair<CACCommonConfig, ForgeConfigSpec> COMMON = new ForgeConfigSpec.Builder().configure(CACCommonConfig::new);

    @Override
    public CACCommonConfig common() {
        return COMMON.getLeft();
    }

    public static void register(ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, COMMON.getRight());
    }

}