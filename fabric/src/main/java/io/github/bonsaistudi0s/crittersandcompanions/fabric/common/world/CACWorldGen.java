package io.github.bonsaistudi0s.crittersandcompanions.fabric.common.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACSpawnConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACTags;

public class CACWorldGen {

    private static final Logger LOGGER = LoggerFactory.getLogger(CACWorldGen.class);

    public static void register() {
        var spawningConfig = CACCommonConfig.HANDLER.instance().spawning;

        for (var entityEntry : spawningConfig.getAllEntries().entrySet()) {
            var entityPath = entityEntry.getKey();
            var type = CACSpawnConfig.resolveEntityType(entityPath);
            if (type == null) {
                LOGGER.warn("Unknown entity '{}' in spawn config, skipping", entityPath);
                continue;
            }

            for (var spawn : entityEntry.getValue()) {
                if (spawn.weight() <= 0) {
                    continue;
                }

                if (spawn.isTag()) {
                    var tag = TagKey.create(Registries.BIOME, spawn.biomeLocation());
                    BiomeModifications.addSpawn(ctx -> ctx.hasTag(tag), type.getCategory(), type, spawn.weight(), spawn.min(), spawn.max());
                } else {
                    var key = ResourceKey.create(Registries.BIOME, spawn.biomeLocation());
                    BiomeModifications.addSpawn(ctx -> ctx.getBiomeKey().equals(key), type.getCategory(), type, spawn.weight(), spawn.min(), spawn.max());
                }
            }
        }

        addFeatureTo(CACTags.SILK_COCOON_SPAWNS, "silk_cocoon");
        addFeatureTo(CACTags.SILK_COCOON_LUSH_SPAWNS, "silk_cocoon_lush");
        addFeatureTo(CACTags.SILK_COCOON_SPAWNS, "hanging_silk_cocoon");
        addFeatureTo(CACTags.SILK_COCOON_LUSH_SPAWNS, "hanging_silk_cocoon_lush");
    }

    private static void addFeatureTo(TagKey<Biome> biome, String feature) {
        BiomeModifications.addFeature(
                it -> it.hasTag(biome),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ResourceKey.create(Registries.PLACED_FEATURE, CrittersAndCompanions.createId(feature))
        );
    }
}
