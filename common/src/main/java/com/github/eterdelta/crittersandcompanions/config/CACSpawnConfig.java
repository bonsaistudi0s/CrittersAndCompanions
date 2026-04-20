package com.github.eterdelta.crittersandcompanions.config;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CACSpawnConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CACSpawnConfig.class);
    public static final String FILE_NAME = "crittersandcompanions-spawns.toml";

    private static CACSpawnConfig INSTANCE;

    // entity id -> list of spawn entries
    private static final Map<String, List<SpawnEntry>> DEFAULTS = new LinkedHashMap<>();

    static {
        defaults(CACEntities.LEAF_INSECT,
                entry(cTag("is_jungle"), 14, 1, 1),
                entry(cTag("is_forest"), 14, 1, 1));
        defaults(CACEntities.RED_PANDA,
                entry(cTag("is_jungle"), 8, 1, 2));
        defaults(CACEntities.JUMPING_SPIDER,
                entry(cTag("is_jungle"), 2, 1, 1),
                entry(cTag("is_forest"), 2, 1, 1),
                entry(cTag("is_lush"), 2, 1, 1));
        defaults(CACEntities.FERRET,
                entry(cTag("is_forest"), 3, 2, 3),
                entry(cTag("is_plains"), 4, 2, 3));
        defaults(CACEntities.SEA_BUNNY,
                entry(Biomes.OCEAN, 16, 1, 2),
                entry(Biomes.DEEP_OCEAN, 16, 1, 2),
                entry(Biomes.WARM_OCEAN, 32, 1, 4),
                entry(Biomes.LUKEWARM_OCEAN, 16, 1, 4),
                entry(Biomes.DEEP_LUKEWARM_OCEAN, 16, 1, 4));
        defaults(CACEntities.DUMBO_OCTOPUS,
                entry(Biomes.OCEAN, 4, 1, 1),
                entry(Biomes.DEEP_OCEAN, 4, 1, 1),
                entry(Biomes.WARM_OCEAN, 8, 1, 1),
                entry(Biomes.LUKEWARM_OCEAN, 6, 1, 1),
                entry(Biomes.DEEP_LUKEWARM_OCEAN, 6, 1, 1));
        defaults(CACEntities.OTTER,
                entry(cTag("is_river"), 1, 3, 5));
        defaults(CACEntities.KOI_FISH,
                entry(cTag("is_river"), 4, 2, 5));
        defaults(CACEntities.DRAGONFLY,
                entry(cTag("is_river"), 7, 1, 1));
        defaults(CACEntities.SHIMA_ENAGA,
                entry(cTag("is_snowy"), 3, 2, 3));
        defaults(CACEntities.LADYBUG,
                entry(cTag("is_forest"), 6, 1, 3),
                entry(cTag("is_lush"), 6, 1, 3));
        defaults(CACEntities.STAG_BEETLE,
                entry(cTag("is_forest"), 4, 1, 2),
                entry(cTag("is_lush"), 4, 1, 2));
        defaults(CACEntities.ROLY_POLY,
                entry(cTag("is_forest"), 5, 1, 3),
                entry(cTag("is_lush"), 5, 1, 3));
        defaults(CACEntities.SNAIL,
                entry(cTag("is_forest"), 5, 1, 2),
                entry(cTag("is_lush"), 5, 1, 2));
        defaults(CACEntities.STICK_BUG,
                entry(cTag("is_forest"), 4, 1, 2),
                entry(cTag("is_lush"), 4, 1, 2));
        defaults(CACEntities.WEEVIL,
                entry(cTag("is_forest"), 4, 1, 3),
                entry(cTag("is_lush"), 4, 1, 3));
    }

    private static TagKey<Biome> cTag(String path) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    private static SpawnEntry entry(TagKey<Biome> tag, int weight, int min, int max) {
        return new SpawnEntry("#" + tag.location(), weight, min, max);
    }

    private static SpawnEntry entry(ResourceKey<Biome> biome, int weight, int min, int max) {
        return new SpawnEntry(biome.location().toString(), weight, min, max);
    }

    private static void defaults(RegistryEntry<? extends EntityType<?>> entity, SpawnEntry... entries) {
        DEFAULTS.put(entity.getKey().location().getPath(), List.of(entries));
    }

    public record SpawnEntry(String biomeSpec, int weight, int min, int max) {

        public boolean isTag() {
            return biomeSpec.startsWith("#");
        }

        public ResourceLocation biomeLocation() {
            return ResourceLocation.parse(isTag() ? biomeSpec.substring(1) : biomeSpec);
        }
    }

    private final Map<String, List<SpawnEntry>> entries;

    private CACSpawnConfig(Map<String, List<SpawnEntry>> entries) {
        this.entries = entries;
    }

    public static CACSpawnConfig get() {
        if (INSTANCE == null) throw new IllegalStateException("CACSpawnConfig not yet loaded");
        return INSTANCE;
    }

    public static void load(Path configDir) {
        Path path = configDir.resolve(FILE_NAME);
        if (!Files.exists(path)) {
            writeDefaults(path);
        }
        INSTANCE = read(path);
        LOGGER.info("Loaded spawn config from {}", path);
    }

    public List<SpawnEntry> getEntries(String entityId) {
        return entries.getOrDefault(entityId, List.of());
    }

    public Map<String, List<SpawnEntry>> getAllEntries() {
        return entries;
    }

    public static EntityType<?> resolveEntityType(String entityId) {
        return BuiltInRegistries.ENTITY_TYPE
                .getOptional(ResourceLocation.fromNamespaceAndPath(CrittersAndCompanions.MODID, entityId))
                .orElse(null);
    }

    private static CACSpawnConfig read(Path path) {
        var result = new LinkedHashMap<String, List<SpawnEntry>>();
        try (var fileConfig = CommentedFileConfig.of(path.toFile())) {
            fileConfig.load();
            for (var mapEntry : fileConfig.entrySet()) {
                String entityId = mapEntry.getKey();
                Object value = mapEntry.getValue();
                if (!(value instanceof List<?> list)) continue;
                var parsed = new ArrayList<SpawnEntry>();
                for (Object item : list) {
                    if (!(item instanceof UnmodifiableConfig entry)) continue;
                    try {
                        String biome = entry.get("biome");
                        int weight = ((Number) entry.get("weight")).intValue();
                        int min = ((Number) entry.get("min")).intValue();
                        int max = ((Number) entry.get("max")).intValue();
                        parsed.add(new SpawnEntry(biome, weight, min, max));
                    } catch (Exception e) {
                        LOGGER.warn("Skipping invalid spawn entry under [{}]: {}", entityId, e.getMessage());
                    }
                }
                if (!parsed.isEmpty()) {
                    result.put(entityId, parsed);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to read spawn config '{}', falling back to defaults", path, e);
            return new CACSpawnConfig(new LinkedHashMap<>(DEFAULTS));
        }
        return new CACSpawnConfig(result);
    }

    private static void writeDefaults(Path path) {
        var sb = new StringBuilder();
        sb.append("# Critters and Companions - Spawn Configuration\n");
        sb.append("# Each [[entity_name]] block defines one biome spawn entry for that entity.\n");
        sb.append("#\n");
        sb.append("# biome  : A biome ID (e.g. \"minecraft:forest\") or a biome tag prefixed with # (e.g. \"#minecraft:is_forest\")\n");
        sb.append("# weight : Spawn weight relative to other mobs in the same category. Set to 0 to disable.\n");
        sb.append("# min    : Minimum number of entities spawned per group.\n");
        sb.append("# max    : Maximum number of entities spawned per group.\n");
        sb.append("#\n");
        sb.append("# You can freely add, remove, or modify entries. Custom biomes and tags are supported.\n");
        sb.append("# Delete this file to regenerate it with the mod defaults on the next start.\n");

        for (var entityEntry : DEFAULTS.entrySet()) {
            String entity = entityEntry.getKey();
            sb.append("\n# ").append(CrittersAndCompanions.MODID).append(":").append(entity).append("\n");
            for (SpawnEntry spawn : entityEntry.getValue()) {
                sb.append("[[").append(entity).append("]]\n");
                sb.append("biome  = \"").append(spawn.biomeSpec()).append("\"\n");
                sb.append("weight = ").append(spawn.weight()).append("\n");
                sb.append("min    = ").append(spawn.min()).append("\n");
                sb.append("max    = ").append(spawn.max()).append("\n");
            }
        }

        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, sb.toString());
        } catch (IOException e) {
            LOGGER.error("Failed to write default spawn config to '{}'", path, e);
        }
    }
}
