package io.github.bonsaistudi0s.crittersandcompanions.common.config;

import com.google.common.base.CaseFormat;
import dev.architectury.platform.Platform;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.DoubleFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACSpawnConfig.SpawnEntry;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACSpawnConfig.SpawnEntryController;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biomes;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.bonsaistudi0s.crittersandcompanions.common.config.CACSpawnConfig.biome;
import static io.github.bonsaistudi0s.crittersandcompanions.common.config.CACSpawnConfig.biomeTag;

public class CACCommonConfig {

    public static final ConfigClassHandler<CACCommonConfig> HANDLER = ConfigClassHandler.createBuilder(CACCommonConfig.class)
            .id(CrittersAndCompanions.createId("common_config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(Platform.getConfigFolder().resolve("crittersandcompanions-common.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public final SpawningConfig spawning = new SpawningConfig();

    @SerialEntry
    public final NecklaceConfig necklace = new NecklaceConfig();

    @SerialEntry
    public final GrapplingHookConfig grapplingHook = new GrapplingHookConfig();

    @SuppressWarnings("unused")
    public static class SpawningConfig {

        // Just add any new entries as new fields below here, they'll be resolved via reflection
        // (transform snake_case entity id to camelCase field name)

        @SerialEntry
        public List<SpawnEntry> leafInsect = List.of(
                biomeTag(BiomeTags.IS_JUNGLE, 14, 1, 1),
                biomeTag(BiomeTags.IS_FOREST, 14, 1, 1));
        @SerialEntry
        public List<SpawnEntry> redPanda = List.of(
                biomeTag(BiomeTags.IS_JUNGLE, 8, 1, 2));
        @SerialEntry
        public List<SpawnEntry> jumpingSpider = List.of(
                biomeTag(BiomeTags.IS_JUNGLE, 2, 1, 1),
                biomeTag(BiomeTags.IS_FOREST, 2, 1, 1),
                biome(Biomes.LUSH_CAVES, 4, 1, 1));
        @SerialEntry
        public List<SpawnEntry> ferret = List.of(
                biomeTag(BiomeTags.IS_FOREST, 3, 2, 3),
                biome(Biomes.PLAINS, 4, 2, 3));
        @SerialEntry
        public List<SpawnEntry> seaBunny = List.of(
                biome(Biomes.OCEAN, 16, 1, 2),
                biome(Biomes.DEEP_OCEAN, 16, 1, 2),
                biome(Biomes.WARM_OCEAN, 32, 1, 4),
                biome(Biomes.LUKEWARM_OCEAN, 16, 1, 4),
                biome(Biomes.DEEP_LUKEWARM_OCEAN, 16, 1, 4));
        @SerialEntry
        public List<SpawnEntry> dumboOctopus = List.of(
                biome(Biomes.OCEAN, 4, 1, 1),
                biome(Biomes.DEEP_OCEAN, 4, 1, 1),
                biome(Biomes.WARM_OCEAN, 8, 1, 1),
                biome(Biomes.LUKEWARM_OCEAN, 6, 1, 1),
                biome(Biomes.DEEP_LUKEWARM_OCEAN, 6, 1, 1));
        @SerialEntry
        public List<SpawnEntry> otter = List.of(
                biomeTag(BiomeTags.IS_RIVER, 1, 2, 4));
        @SerialEntry
        public List<SpawnEntry> koiFish = List.of(
                biomeTag(BiomeTags.IS_RIVER, 2, 2, 5));
        @SerialEntry
        public List<SpawnEntry> dragonfly = List.of(
                biomeTag(BiomeTags.IS_RIVER, 4, 1, 1),
                biome(Biomes.SWAMP, 5, 1, 1),
                biome(Biomes.MANGROVE_SWAMP, 5, 1, 1),
                biome(Biomes.LUSH_CAVES, 10, 1, 1));
        @SerialEntry
        public List<SpawnEntry> shimaEnaga = List.of(
                biomeTag(BiomeTags.SPAWNS_SNOW_FOXES, 3, 2, 3));
        @SerialEntry
        public List<SpawnEntry> ladybug = List.of(
                biomeTag(BiomeTags.IS_FOREST, 6, 1, 3),
                biome(Biomes.LUSH_CAVES, 12, 1, 2),
                biome(Biomes.CHERRY_GROVE, 12, 1, 2),
                biome(Biomes.SUNFLOWER_PLAINS, 12, 1, 2),
                biome(Biomes.MEADOW, 12, 1, 2));
        @SerialEntry
        public List<SpawnEntry> stagBeetle = List.of(
                biomeTag(BiomeTags.IS_FOREST, 4, 1, 2),
                biome(Biomes.LUSH_CAVES, 8, 1, 2));
        @SerialEntry
        public List<SpawnEntry> rolyPoly = List.of(
                biomeTag(BiomeTags.IS_FOREST, 5, 1, 3),
                biome(Biomes.LUSH_CAVES, 10, 1, 2),
                biome(Biomes.SWAMP, 10, 1, 2));
        @SerialEntry
        public List<SpawnEntry> snail = List.of(
                biomeTag(BiomeTags.IS_FOREST, 5, 1, 2),
                biome(Biomes.LUSH_CAVES, 10, 1, 2),
                biome(Biomes.SWAMP, 10, 1, 2));
        @SerialEntry
        public List<SpawnEntry> stickBug = List.of(
                biomeTag(BiomeTags.IS_FOREST, 4, 1, 2),
                biome(Biomes.LUSH_CAVES, 4, 1, 2));
        @SerialEntry
        public List<SpawnEntry> weevil = List.of(
                biomeTag(BiomeTags.IS_FOREST, 4, 1, 3),
                biome(Biomes.LUSH_CAVES, 4, 1, 2));

        private static final List<Field> ENTITY_FIELDS = Arrays.stream(SpawningConfig.class.getDeclaredFields())
                .filter(field -> !java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.getType() == List.class)
                .toList();

        public Map<String, List<SpawnEntry>> getAllEntries() {
            Map<String, List<SpawnEntry>> entries = new LinkedHashMap<>();
            for (Field field : ENTITY_FIELDS) {
                entries.put(entityId(field), get(this, field));
            }
            return entries;
        }

        public ConfigCategory buildCategory() {
            var builder = ConfigCategory.createBuilder()
                    .name(Component.literal("Spawning"));

            SpawningConfig defaults = HANDLER.defaults().spawning;
            for (var field : ENTITY_FIELDS) {
                builder.group(buildGroup(field, defaults));
            }

            return builder.build();
        }

        private ListOption<SpawnEntry> buildGroup(Field field, SpawningConfig defaults) {
            return ListOption.<SpawnEntry>createBuilder()
                    .name(displayName(field))
                    .description(OptionDescription.of(Component.literal(
                            """
                                    Each entry is one biome spawn rule in the format "biome;weight;min;max".
                                    
                                    Use a biome id (e.g. minecraft:forest) or a tag prefixed with # (e.g. #c:is_forest).
                                    
                                    Weight is relative to other mobs in the same category. Min/max define the group size range.
                                    """)))
                    .binding(get(defaults, field), () -> get(this, field), v -> set(this, field, v))
                    .initial(() -> new SpawnEntry("minecraft:plains", 1, 1, 1))
                    .customController(SpawnEntryController::new)
                    .insertEntriesAtEnd(true)
                    .collapsed(true)
                    .build();
        }

        private static String entityId(Field field) {
            return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }

        private static Component displayName(Field field) {
            return Component.translatable("entity.crittersandcompanions." + entityId(field));
        }

        @SuppressWarnings("unchecked")
        private static List<SpawnEntry> get(SpawningConfig config, Field field) {
            try {
                return (List<SpawnEntry>) field.get(config);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private static void set(SpawningConfig config, Field field, List<SpawnEntry> value) {
            try {
                field.set(config, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class NecklaceConfig {
        @SerialEntry
        public double swimSpeed = 0.2;
        @SerialEntry
        public double drownedRangeDebuff = 0.1;
        @SerialEntry
        public double guardianRangeDebuff = 0.1;

        public ConfigCategory buildCategory() {
            return ConfigCategory.createBuilder()
                    .name(Component.literal("Necklace"))

                    .option(Option.<Double>createBuilder()
                            .name(Component.literal("Swim Speed"))
                            .description(OptionDescription.of(Component.literal("Multiplier for swimming speed.")))
                            .binding(0.2, () -> this.swimSpeed, newVal -> this.swimSpeed = newVal)
                            .controller(opt -> DoubleSliderControllerBuilder.create(opt).range(0.0, 1.0).step(0.05))
                            .build())

                    .option(Option.<Double>createBuilder()
                            .name(Component.literal("Drowned Range Debuff"))
                            .binding(0.1, () -> this.drownedRangeDebuff, newVal -> this.drownedRangeDebuff = newVal)
                            .controller(opt -> DoubleSliderControllerBuilder.create(opt).range(0.0, 1.0).step(0.05))
                            .build())

                    .option(Option.<Double>createBuilder()
                            .name(Component.literal("Guardian Range Debuff"))
                            .binding(0.1, () -> this.guardianRangeDebuff, newVal -> this.guardianRangeDebuff = newVal)
                            .controller(opt -> DoubleSliderControllerBuilder.create(opt).range(0.0, 1.0).step(0.05))
                            .build())

                    .build();
        }

        public double rangeDebuff(EntityType<?> type, int level) {
            if (type == EntityType.GUARDIAN && level > 1) {
                return guardianRangeDebuff * level;
            }
            if (type == EntityType.DROWNED) {
                return drownedRangeDebuff * level;
            }

            return 0;
        }
    }

    public static class GrapplingHookConfig {
        @SerialEntry
        public double launchSpeed = 1.0;
        @SerialEntry
        public double maxSpeed = 4.0;
        @SerialEntry
        public double maxDistance = 32.0;

        public ConfigCategory buildCategory() {
            return ConfigCategory.createBuilder()
                    .name(Component.literal("Grappling Hook"))

                    .option(Option.<Double>createBuilder()
                            .name(Component.literal("Launch Speed"))
                            .binding(1.0, () -> this.launchSpeed, newVal -> this.launchSpeed = newVal)
                            .controller(opt -> DoubleSliderControllerBuilder.create(opt).range(0.0, 10.0).step(0.5))
                            .build())

                    .option(Option.<Double>createBuilder()
                            .name(Component.literal("Max Speed"))
                            .binding(4.0, () -> this.maxSpeed, newVal -> this.maxSpeed = newVal)
                            .controller(opt -> DoubleFieldControllerBuilder.create(opt).range(1.0, 1000.0))
                            .build())

                    .option(Option.<Double>createBuilder()
                            .name(Component.literal("Max Distance"))
                            .binding(32.0, () -> this.maxDistance, newVal -> this.maxDistance = newVal)
                            .controller(opt -> DoubleSliderControllerBuilder.create(opt).range(4.0, 128.0).step(1.0))
                            .build())

                    .build();
        }
    }

    public static Screen createConfigScreen(Screen parent) {
        var config = HANDLER.instance();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Critters and Companions Config"))
                .save(HANDLER::save)
                .category(config.spawning.buildCategory())
                .category(config.necklace.buildCategory())
                .category(config.grapplingHook.buildCategory())
                .build()
                .generateScreen(parent);
    }
}
