package com.github.eterdelta.crittersandcompanions.handler;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import com.github.eterdelta.crittersandcompanions.entity.SeaBunnyEntity;
import com.github.eterdelta.crittersandcompanions.registry.CaCEntities;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID)
public class SpawnHandler {

    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        if (event.getName() != null) {
            ResourceKey<Biome> biome = ResourceKey.create(ForgeRegistries.Keys.BIOMES, event.getName());

            if (biome == Biomes.RIVER) {
                event.getSpawns().getSpawner(MobCategory.WATER_CREATURE).add(new MobSpawnSettings.SpawnerData(CaCEntities.OTTER.get(), 2, 3, 5));
                event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.KOI_FISH.get(), 4, 2, 5));
                event.getSpawns().getSpawner(MobCategory.AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.DRAGONFLY.get(), 6, 1, 1));
            } else if (biome == Biomes.OCEAN) {
                event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.SEA_BUNNY.get(), 6, 1, 1));
            } else if (biome == Biomes.LUKEWARM_OCEAN) {
                event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.SEA_BUNNY.get(), 6, 1, 1));
            } else if (biome == Biomes.WARM_OCEAN) {
                event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.SEA_BUNNY.get(), 12, 1, 1));
            }
        }
    }

    public static void registerSpawnPlacements() {
        SpawnPlacements.register(CaCEntities.OTTER.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OtterEntity::checkOtterSpawnRules);
        SpawnPlacements.register(CaCEntities.KOI_FISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(CaCEntities.DRAGONFLY.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, DragonflyEntity::checkDragonflySpawnRules);
        SpawnPlacements.register(CaCEntities.SEA_BUNNY.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SeaBunnyEntity::checkSeaBunnySpawnRules);
    }
}
