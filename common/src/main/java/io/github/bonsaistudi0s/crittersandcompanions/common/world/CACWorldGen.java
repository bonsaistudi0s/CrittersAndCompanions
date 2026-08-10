package io.github.bonsaistudi0s.crittersandcompanions.common.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

import dev.architectury.registry.level.biome.BiomeModifications;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACTags;

public class CACWorldGen {

    public static void register() {
        addFeatureTo(CACTags.SILK_COCOON_SPAWNS, "silk_cocoon");
        addFeatureTo(CACTags.SILK_COCOON_LUSH_SPAWNS, "silk_cocoon_lush");
        addFeatureTo(CACTags.SILK_COCOON_SPAWNS, "hanging_silk_cocoon");
        addFeatureTo(CACTags.SILK_COCOON_LUSH_SPAWNS, "hanging_silk_cocoon_lush");
    }

    private static void addFeatureTo(TagKey<Biome> biome, String feature) {
        BiomeModifications.addProperties(context -> context.hasTag(biome),
                (context, properties) -> properties.getGenerationProperties().addFeature(
                        GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                        ResourceKey.create(Registries.PLACED_FEATURE, CrittersAndCompanions.createId(feature))
                )
        );
    }
}
