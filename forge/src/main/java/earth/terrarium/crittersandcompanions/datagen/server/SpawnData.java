package earth.terrarium.crittersandcompanions.datagen.server;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.registry.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpawnData {
    public static void datagenBiomeModifiers(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        RegistryAccess access = RegistryAccess.EMPTY;
        Registry<Biome> registry = access.registryOrThrow(Registries.BIOME);

        Map<ResourceLocation, BiomeModifier> modifierMap = new HashMap<>();

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_river_spawns"),
            createSpawnModifier(getBiomeHolderSet(registry, Biomes.RIVER),
                new MobSpawnSettings.SpawnerData(ModEntities.OTTER.get(), 2, 3, 5),
                new MobSpawnSettings.SpawnerData(ModEntities.KOI_FISH.get(), 4, 2, 5),
                new MobSpawnSettings.SpawnerData(ModEntities.DRAGONFLY.get(), 6, 1, 1)
            )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_oceans_spawns"),
            createSpawnModifier(getBiomeHolderSet(registry, Biomes.OCEAN, Biomes.DEEP_OCEAN),
                new MobSpawnSettings.SpawnerData(ModEntities.SEA_BUNNY.get(), 32, 1, 2),
                new MobSpawnSettings.SpawnerData(ModEntities.DUMBO_OCTOPUS.get(), 4, 1, 1)
            )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_lukewarm_oceans_spawns"),
            createSpawnModifier(getBiomeHolderSet(registry, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN),
                new MobSpawnSettings.SpawnerData(ModEntities.SEA_BUNNY.get(), 32, 1, 3),
                new MobSpawnSettings.SpawnerData(ModEntities.DUMBO_OCTOPUS.get(), 6, 1, 1)
            )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_warm_ocean_spawns"),
            createSpawnModifier(getBiomeHolderSet(registry, Biomes.WARM_OCEAN),
                new MobSpawnSettings.SpawnerData(ModEntities.SEA_BUNNY.get(), 64, 1, 4),
                new MobSpawnSettings.SpawnerData(ModEntities.DUMBO_OCTOPUS.get(), 8, 1, 1)
            )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_forests_spawns"),
            createSpawnModifier(registry.getOrCreateTag(BiomeTags.IS_FOREST),
                new MobSpawnSettings.SpawnerData(ModEntities.LEAF_INSECT.get(), 12, 1, 1),
                new MobSpawnSettings.SpawnerData(ModEntities.FERRET.get(), 3, 2, 3),
                new MobSpawnSettings.SpawnerData(ModEntities.JUMPING_SPIDER.get(), 2, 1, 1)
            )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_jungles_spawns"),
            createSpawnModifier(registry.getOrCreateTag(BiomeTags.IS_JUNGLE),
                new MobSpawnSettings.SpawnerData(ModEntities.LEAF_INSECT.get(), 12, 1, 1),
                new MobSpawnSettings.SpawnerData(ModEntities.RED_PANDA.get(), 8, 1, 2),
                new MobSpawnSettings.SpawnerData(ModEntities.JUMPING_SPIDER.get(), 2, 1, 1)
            )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_plains_spawns"),
            createSpawnModifier(getBiomeHolderSet(registry, Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS),
                new MobSpawnSettings.SpawnerData(ModEntities.FERRET.get(), 4, 2, 3)
            )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_snowy_plains_spawns"),
            createSpawnModifier(getBiomeHolderSet(registry, Biomes.SNOWY_PLAINS),
                new MobSpawnSettings.SpawnerData(ModEntities.SHIMA_ENAGA.get(), 3, 2, 3)
            )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_lush_caves_spawns"),
            createSpawnModifier(getBiomeHolderSet(registry, Biomes.LUSH_CAVES),
                new MobSpawnSettings.SpawnerData(ModEntities.JUMPING_SPIDER.get(), 2, 1, 1)
            )
        );

        final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, access);

        generator.addProvider(event.includeServer(), new JsonCodecProvider<>(
            generator.getPackOutput(), existingFileHelper, CrittersAndCompanions.MODID, ops, PackType.SERVER_DATA, "forge/biome_modifier", BiomeModifier.DIRECT_CODEC, modifierMap));
    }

    @SafeVarargs
    public static HolderSet<Biome> getBiomeHolderSet(Registry<Biome> registry, ResourceKey<Biome>... biomes) {
        return HolderSet.direct(Arrays.stream(biomes).map(registry::getHolderOrThrow).toList());
    }

    public static BiomeModifier createSpawnModifier(HolderSet<Biome> biomes, MobSpawnSettings.SpawnerData... spawnerData) {
        return (spawnerData.length == 1)
            ? ForgeBiomeModifiers.AddSpawnsBiomeModifier.singleSpawn(biomes, spawnerData[0])
            : new ForgeBiomeModifiers.AddSpawnsBiomeModifier(biomes, Arrays.stream(spawnerData).toList());
    }
}
