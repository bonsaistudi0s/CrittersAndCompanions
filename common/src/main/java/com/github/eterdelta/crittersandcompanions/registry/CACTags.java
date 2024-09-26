package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class CACTags {

    public static final TagKey<Biome> SILK_COCOON_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(CrittersAndCompanions.MODID, "silk_cocoon_spawns"));
    public static final TagKey<Biome> SILK_COCOON_LUSH_SPAWNS = TagKey.create(Registries.BIOME, new ResourceLocation(CrittersAndCompanions.MODID, "silk_cocoon_lush_spawns"));

}
