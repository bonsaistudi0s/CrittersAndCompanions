package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.function.Supplier;

public class CACWorldGen {

    public static void register() {
        // should always reflect the values in forge/biome_modifiers/..

        addSpawnsTo(BiomeTags.IS_JUNGLE, CACEntities.LEAF_INSECT, 14, 1, 1);
        addSpawnsTo(BiomeTags.IS_JUNGLE, CACEntities.RED_PANDA, 8, 1, 2);
        addSpawnsTo(BiomeTags.IS_JUNGLE, CACEntities.JUMPING_SPIDER, 2, 1, 1);
        addSpawnsTo(Biomes.LUSH_CAVES, CACEntities.JUMPING_SPIDER, 2, 1, 1);
        addSpawnsTo(Biomes.OCEAN, CACEntities.SEA_BUNNY, 16, 1, 2);
        addSpawnsTo(Biomes.OCEAN, CACEntities.DUMBO_OCTOPUS, 4, 1, 1);
        addSpawnsTo(Biomes.DEEP_OCEAN, CACEntities.SEA_BUNNY, 16, 1, 2);
        addSpawnsTo(Biomes.DEEP_OCEAN, CACEntities.DUMBO_OCTOPUS, 4, 1, 1);
        addSpawnsTo(BiomeTags.HAS_VILLAGE_PLAINS, CACEntities.FERRET, 4, 2, 3);
        addSpawnsTo(Biomes.SUNFLOWER_PLAINS, CACEntities.FERRET, 4, 2, 3);
        addSpawnsTo(BiomeTags.IS_RIVER, CACEntities.OTTER, 1, 3, 5);
        addSpawnsTo(BiomeTags.IS_RIVER, CACEntities.KOI_FISH, 4, 2, 5);
        addSpawnsTo(BiomeTags.IS_RIVER, CACEntities.DRAGONFLY, 14, 1, 1);
        addSpawnsTo(Biomes.SNOWY_PLAINS, CACEntities.SHIMA_ENAGA, 3, 2, 3);
        addSpawnsTo(Biomes.WARM_OCEAN, CACEntities.SEA_BUNNY, 32, 1, 4);
        addSpawnsTo(Biomes.WARM_OCEAN, CACEntities.DUMBO_OCTOPUS, 8, 1, 1);
        addSpawnsTo(Biomes.LUKEWARM_OCEAN, CACEntities.SEA_BUNNY, 16, 1, 4);
        addSpawnsTo(Biomes.LUKEWARM_OCEAN, CACEntities.DUMBO_OCTOPUS, 6, 1, 1);
        addSpawnsTo(Biomes.DEEP_LUKEWARM_OCEAN, CACEntities.SEA_BUNNY, 16, 1, 4);
        addSpawnsTo(Biomes.DEEP_LUKEWARM_OCEAN, CACEntities.DUMBO_OCTOPUS, 6, 1, 1);

        addFeatureTo(CACTags.SILK_COCOON_SPAWNS, "silk_cocoon");
        addFeatureTo(CACTags.SILK_COCOON_LUSH_SPAWNS, "silk_cocoon_lush");
    }

    private static void addFeatureTo(TagKey<Biome> biome, String feature) {
        BiomeModifications.addFeature(
                it -> it.hasTag(biome),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(CrittersAndCompanions.MODID, feature))
        );
    }

    private static void addSpawnsTo(ResourceKey<Biome> biome, Supplier<? extends EntityType<?>> typeSupplier, int weight, int min, int max) {
        var type = typeSupplier.get();
        BiomeModifications.addSpawn(it -> it.getBiomeKey().equals(biome), type.getCategory(), type, weight, min, max);
    }

    private static void addSpawnsTo(TagKey<Biome> biome, Supplier<? extends EntityType<?>> typeSupplier, int weight, int min, int max) {
        var type = typeSupplier.get();
        BiomeModifications.addSpawn(it -> it.hasTag(biome), type.getCategory(), type, weight, min, max);
    }

}
