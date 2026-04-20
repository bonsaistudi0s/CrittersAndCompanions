package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.config.CACSpawnConfig;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CACSpawnsBiomeModifier implements BiomeModifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(CACSpawnsBiomeModifier.class);
    public static final MapCodec<CACSpawnsBiomeModifier> CODEC = MapCodec.unit(new CACSpawnsBiomeModifier());

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) return;

        var spawnConfig = CACSpawnConfig.get();

        for (var entityEntry : spawnConfig.getAllEntries().entrySet()) {
            String entityPath = entityEntry.getKey();
            var type = CACSpawnConfig.resolveEntityType(entityPath);
            if (type == null) {
                LOGGER.warn("Unknown entity '{}' in spawn config, skipping", entityPath);
                continue;
            }
            for (var spawn : entityEntry.getValue()) {
                if (spawn.weight() <= 0) continue;
                boolean matches = spawn.isTag()
                        ? biome.is(TagKey.create(Registries.BIOME, spawn.biomeLocation()))
                        : biome.is(ResourceKey.create(Registries.BIOME, spawn.biomeLocation()));
                if (!matches) continue;
                builder.getMobSpawnSettings().addSpawn(
                        type.getCategory(),
                        new MobSpawnSettings.SpawnerData(type, spawn.weight(), spawn.min(), spawn.max())
                );
            }
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
