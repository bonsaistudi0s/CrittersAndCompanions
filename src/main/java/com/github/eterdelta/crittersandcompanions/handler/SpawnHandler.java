package com.github.eterdelta.crittersandcompanions.handler;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import com.github.eterdelta.crittersandcompanions.entity.DumboOctopusEntity;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import com.github.eterdelta.crittersandcompanions.entity.SeaBunnyEntity;
import com.github.eterdelta.crittersandcompanions.registry.CaCEntities;
import com.github.eterdelta.crittersandcompanions.registry.CaCItems;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID)
public class SpawnHandler {

    @SubscribeEvent
    public static void onLivingCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.getEntityLiving() instanceof Drowned drowned && event.getSpawnReason() == MobSpawnType.NATURAL && drowned.getRandom().nextFloat() <= 0.05F) {
            drowned.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(CaCItems.CLAM.get()));
        }
    }

    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        if (event.getName() != null) {
            ResourceKey<Biome> biome = ResourceKey.create(ForgeRegistries.Keys.BIOMES, event.getName());

            if (biome == Biomes.RIVER) {
                event.getSpawns().getSpawner(MobCategory.WATER_CREATURE).add(new MobSpawnSettings.SpawnerData(CaCEntities.OTTER.get(), 2, 3, 5));
                event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.KOI_FISH.get(), 4, 2, 5));
                event.getSpawns().getSpawner(MobCategory.AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.DRAGONFLY.get(), 6, 1, 1));
            } else if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN) {
                event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.SEA_BUNNY.get(), 32, 1, 2));
                event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.DUMBO_OCTOPUS.get(), 4, 1, 1));
            } else if (biome == Biomes.LUKEWARM_OCEAN || biome == Biomes.DEEP_LUKEWARM_OCEAN) {
                event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.SEA_BUNNY.get(), 32, 1, 3));
                event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.DUMBO_OCTOPUS.get(), 4, 1, 1));
            } else if (biome == Biomes.WARM_OCEAN) {
                event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.SEA_BUNNY.get(), 64, 1, 4));
                event.getSpawns().getSpawner(MobCategory.WATER_AMBIENT).add(new MobSpawnSettings.SpawnerData(CaCEntities.DUMBO_OCTOPUS.get(), 6, 1, 1));
            } else if (biome == Biomes.BIRCH_FOREST || biome == Biomes.FOREST || biome == Biomes.PLAINS) {
                event.getSpawns().getSpawner(MobCategory.CREATURE).add(new MobSpawnSettings.SpawnerData(CaCEntities.FERRET.get(), 3, 2, 3));
            }
        }
    }

    public static void registerSpawnPlacements() {
        SpawnPlacements.register(CaCEntities.OTTER.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OtterEntity::checkOtterSpawnRules);
        SpawnPlacements.register(CaCEntities.KOI_FISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(CaCEntities.DRAGONFLY.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, DragonflyEntity::checkDragonflySpawnRules);
        SpawnPlacements.register(CaCEntities.SEA_BUNNY.get(), SpawnPlacements.Type.create("ON_WATER_GROUND",
                ((levelReader, blockPos, entityType) -> levelReader.getFluidState(blockPos).is(FluidTags.WATER) && levelReader.getBlockState(blockPos.below()).isFaceSturdy(levelReader, blockPos.below(), Direction.UP))), Heightmap.Types.OCEAN_FLOOR, SeaBunnyEntity::checkSeaBunnySpawnRules);
        SpawnPlacements.register(CaCEntities.FERRET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacements.register(CaCEntities.DUMBO_OCTOPUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DumboOctopusEntity::checkDumboOctopusSpawnRules);
    }
}
