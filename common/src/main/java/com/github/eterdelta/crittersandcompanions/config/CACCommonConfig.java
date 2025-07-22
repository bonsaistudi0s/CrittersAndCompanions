package com.github.eterdelta.crittersandcompanions.config;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class CACCommonConfig {

    public final DoubleValue necklaceGuardianDebuff;
    public final DoubleValue necklaceDrownedDebuff;
    public final DoubleValue necklaceSwimSpeed;

    public double necklaceRangeDebuff(EntityType<?> type, int level) {
        if(type == EntityType.GUARDIAN && level > 1) return necklaceGuardianDebuff.get() * level;
        if(type == EntityType.DROWNED) return necklaceDrownedDebuff.get() * level;
        return 0;
    }

    public CACCommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("necklace");

        this.necklaceSwimSpeed = builder.defineInRange("swim_sped", 0.2F, 0F, 1F);
        this.necklaceDrownedDebuff = builder.defineInRange("drowned_range_debuff", 0.1F, 0F, 1F);
        this.necklaceGuardianDebuff = builder.defineInRange("guardian_range_debuff", 0.1F, 0F, 1F);

        builder.pop();
    }

}
