package io.github.bonsaistudi0s.crittersandcompanions.common.config;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.string.IStringController;
import dev.isxander.yacl3.gui.controllers.string.StringControllerElement;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CACSpawnConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CACSpawnConfig.class);

    public record SpawnEntry(String biomeSpec, int weight, int min, int max) {

        public boolean isTag() {
            return biomeSpec.startsWith("#");
        }

        public ResourceLocation biomeLocation() {
            return new ResourceLocation(isTag() ? biomeSpec.substring(1) : biomeSpec);
        }

        public String format() {
            return biomeSpec + ";" + weight + ";" + min + ";" + max;
        }

        public static SpawnEntry parse(String raw) {
            var parts = raw.split(";", -1);
            if (parts.length != 4) {
                throw new IllegalArgumentException("Expected format: 'biome;weight;min;max'");
            }

            var biomeSpec = parts[0].trim();
            if (biomeSpec.isEmpty()) {
                throw new IllegalArgumentException("Biome id/tag must not be empty");
            }

            // validates that the biome id/tag is a valid resource location
            new ResourceLocation(biomeSpec.startsWith("#") ? biomeSpec.substring(1) : biomeSpec);

            var weight = Integer.parseInt(parts[1].trim());
            var min = Integer.parseInt(parts[2].trim());
            var max = Integer.parseInt(parts[3].trim());
            if (weight < 0) {
                throw new IllegalArgumentException("Weight must be >= 0");
            }
            if (min < 0 || max < min) {
                throw new IllegalArgumentException("Invalid min/max group size");
            }

            return new SpawnEntry(biomeSpec, weight, min, max);
        }
    }

    public record SpawnEntryController(Option<SpawnEntry> option) implements IStringController<SpawnEntry> {

        @Override
        public String getString() {
            return option().pendingValue().format();
        }

        @Override
        public void setFromString(String value) {
            try {
                option().requestSet(SpawnEntry.parse(value));
            } catch (Exception e) {
                LOGGER.debug("Ignoring incomplete/invalid spawn entry '{}': {}", value, e.getMessage());
            }
        }

        @Override
        public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
            // commit on defocus/submit rather than per keystroke
            return new StringControllerElement(this, screen, widgetDimension, false);
        }
    }

    public static SpawnEntry biome(ResourceKey<Biome> biome, int weight, int min, int max) {
        return new SpawnEntry(biome.location().toString(), weight, min, max);
    }

    public static SpawnEntry biomeTag(TagKey<Biome> biome, int weight, int min, int max) {
        return new SpawnEntry("#" + biome.location(), weight, min, max);
    }

    public static EntityType<?> resolveEntityType(String entityId) {
        return BuiltInRegistries.ENTITY_TYPE
                .getOptional(CrittersAndCompanions.createId(entityId))
                .orElse(null);
    }
}
