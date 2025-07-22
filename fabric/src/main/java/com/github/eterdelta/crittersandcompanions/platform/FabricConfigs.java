package com.github.eterdelta.crittersandcompanions.platform;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.config.CACCommonConfig;
import com.github.eterdelta.crittersandcompanions.config.FabricCommonConfig;
import com.github.eterdelta.crittersandcompanions.platform.service.IConfigs;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class FabricConfigs implements IConfigs {

    private static final Pair<FabricCommonConfig, ForgeConfigSpec> COMMON = new ForgeConfigSpec.Builder().configure(FabricCommonConfig::new);

    public static void register() {
        ForgeConfigRegistry.INSTANCE.register(CrittersAndCompanions.MODID, ModConfig.Type.COMMON, COMMON.getRight());
    }

    @Override
    public CACCommonConfig common() {
        return COMMON.getLeft();
    }

}