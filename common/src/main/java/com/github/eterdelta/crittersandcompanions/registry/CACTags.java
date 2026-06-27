package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class CACTags {

    public static final TagKey<Biome> SILK_COCOON_SPAWNS = TagKey.create(Registries.BIOME, CrittersAndCompanions.createId("silk_cocoon_spawns"));
    public static final TagKey<Biome> SILK_COCOON_LUSH_SPAWNS = TagKey.create(Registries.BIOME, CrittersAndCompanions.createId("silk_cocoon_lush_spawns"));

    public static final TagKey<Item> WOODEN_CHESTS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "chests/wooden"));

    public static final TagKey<Block> BUG_SPAWN_GROUND = TagKey.create(Registries.BLOCK, CrittersAndCompanions.createId("bug_spawn_ground"));

}
