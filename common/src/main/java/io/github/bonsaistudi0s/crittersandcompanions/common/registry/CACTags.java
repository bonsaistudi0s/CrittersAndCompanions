package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class CACTags {

    public static final TagKey<Biome> SILK_COCOON_SPAWNS = TagKey.create(Registries.BIOME, CrittersAndCompanions.createId("silk_cocoon_spawns"));
    public static final TagKey<Biome> SILK_COCOON_LUSH_SPAWNS = TagKey.create(Registries.BIOME, CrittersAndCompanions.createId("silk_cocoon_lush_spawns"));

    public static final TagKey<Item> WOODEN_CHESTS = TagKey.create(Registries.ITEM, CrittersAndCompanions.createId("chests/wooden"));

    public static final TagKey<Block> BUG_SPAWN_GROUND = TagKey.create(Registries.BLOCK, CrittersAndCompanions.createId("bug_spawn_ground"));

    public static final TagKey<DamageType> PANIC_CAUSES = TagKey.create(Registries.DAMAGE_TYPE, CrittersAndCompanions.createId("panic_causes"));;
    public static final TagKey<DamageType> PANIC_ENVIRONMENTAL_CAUSES = TagKey.create(Registries.DAMAGE_TYPE, CrittersAndCompanions.createId("panic_environmental_causes"));;

}
