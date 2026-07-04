package io.github.bonsaistudi0s.crittersandcompanions.neoforge.common.world;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACSpawnConfig;

public class CACSpawnsBiomeModifier implements BiomeModifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(CACSpawnsBiomeModifier.class);
    public static final MapCodec<CACSpawnsBiomeModifier> CODEC = MapCodec.unit(new CACSpawnsBiomeModifier());

    @Override
    public void modify(@NotNull Holder<Biome> biome, @NotNull Phase phase, @NotNull ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) {
            return;
        }

        var spawningConfig = CACCommonConfig.HANDLER.instance().spawning;

        for (var entityEntry : spawningConfig.getAllEntries().entrySet()) {
            String entityPath = entityEntry.getKey();
            var type = CACSpawnConfig.resolveEntityType(entityPath);
            if (type == null) {
                LOGGER.warn("Unknown entity '{}' in spawn config, skipping", entityPath);
                continue;
            }

            for (var spawn : entityEntry.getValue()) {
                if (spawn.weight() <= 0) {
                    continue;
                }

                var matches = spawn.isTag()
                        ? biome.is(TagKey.create(Registries.BIOME, spawn.biomeLocation()))
                        : biome.is(ResourceKey.create(Registries.BIOME, spawn.biomeLocation()));

                if (!matches) {
                    continue;
                }

                builder.getMobSpawnSettings().addSpawn(
                        type.getCategory(),
                        new MobSpawnSettings.SpawnerData(type, spawn.weight(), spawn.min(), spawn.max())
                );
            }
        }
    }

    @Override
    public @NotNull MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
