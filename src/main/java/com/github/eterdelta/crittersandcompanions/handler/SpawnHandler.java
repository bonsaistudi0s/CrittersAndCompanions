package com.github.eterdelta.crittersandcompanions.handler;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.*;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID)
public class SpawnHandler {

    @SubscribeEvent
    public static void onLivingCheckSpawn(MobSpawnEvent.PositionCheck event) {
        if (event.getEntity() instanceof Drowned drowned && event.getSpawnType() == MobSpawnType.NATURAL && drowned.getRandom().nextFloat() <= 0.05F) {
            drowned.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(CACItems.CLAM.get()));
        }
    }

    public static void datagenBiomeModifiers(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        /*
        RegistryAccess registryAccess = RegistryAccess.builtinCopy();
        Registry<Biome> registry = registryAccess.registryOrThrow(Registry.BIOME_REGISTRY);

        Map<ResourceLocation, BiomeModifier> modifierMap = new HashMap<>();

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_river_spawns"),
                createSpawnModifier(getBiomeHolderSet(registry, Biomes.RIVER),
                        new MobSpawnSettings.SpawnerData(CACEntities.OTTER.get(), 2, 3, 5),
                        new MobSpawnSettings.SpawnerData(CACEntities.KOI_FISH.get(), 4, 2, 5),
                        new MobSpawnSettings.SpawnerData(CACEntities.DRAGONFLY.get(), 6, 1, 1)
                )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_oceans_spawns"),
                createSpawnModifier(getBiomeHolderSet(registry, Biomes.OCEAN, Biomes.DEEP_OCEAN),
                        new MobSpawnSettings.SpawnerData(CACEntities.SEA_BUNNY.get(), 32, 1, 2),
                        new MobSpawnSettings.SpawnerData(CACEntities.DUMBO_OCTOPUS.get(), 4, 1, 1)
                )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_lukewarm_oceans_spawns"),
                createSpawnModifier(getBiomeHolderSet(registry, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN),
                        new MobSpawnSettings.SpawnerData(CACEntities.SEA_BUNNY.get(), 32, 1, 3),
                        new MobSpawnSettings.SpawnerData(CACEntities.DUMBO_OCTOPUS.get(), 6, 1, 1)
                )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_warm_ocean_spawns"),
                createSpawnModifier(getBiomeHolderSet(registry, Biomes.WARM_OCEAN),
                        new MobSpawnSettings.SpawnerData(CACEntities.SEA_BUNNY.get(), 64, 1, 4),
                        new MobSpawnSettings.SpawnerData(CACEntities.DUMBO_OCTOPUS.get(), 8, 1, 1)
                )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_forests_spawns"),
                createSpawnModifier(registry.getOrCreateTag(BiomeTags.IS_FOREST),
                        new MobSpawnSettings.SpawnerData(CACEntities.LEAF_INSECT.get(), 12, 1, 1),
                        new MobSpawnSettings.SpawnerData(CACEntities.FERRET.get(), 3, 2, 3),
                        new MobSpawnSettings.SpawnerData(CACEntities.JUMPING_SPIDER.get(), 2, 1, 1)
                )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_jungles_spawns"),
                createSpawnModifier(registry.getOrCreateTag(BiomeTags.IS_JUNGLE),
                        new MobSpawnSettings.SpawnerData(CACEntities.LEAF_INSECT.get(), 12, 1, 1),
                        new MobSpawnSettings.SpawnerData(CACEntities.RED_PANDA.get(), 8, 1, 2),
                        new MobSpawnSettings.SpawnerData(CACEntities.JUMPING_SPIDER.get(), 2, 1, 1)
                )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_plains_spawns"),
                createSpawnModifier(getBiomeHolderSet(registry, Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS),
                        new MobSpawnSettings.SpawnerData(CACEntities.FERRET.get(), 4, 2, 3)
                )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_snowy_plains_spawns"),
                createSpawnModifier(getBiomeHolderSet(registry, Biomes.SNOWY_PLAINS),
                        new MobSpawnSettings.SpawnerData(CACEntities.SHIMA_ENAGA.get(), 3, 2, 3)
                )
        );

        modifierMap.put(new ResourceLocation(CrittersAndCompanions.MODID, "add_lush_caves_spawns"),
                createSpawnModifier(getBiomeHolderSet(registry, Biomes.LUSH_CAVES),
                        new MobSpawnSettings.SpawnerData(CACEntities.JUMPING_SPIDER.get(), 2, 1, 1)
                )
        );

        final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registryAccess);

        generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
                generator, existingFileHelper, CrittersAndCompanions.MODID, ops, ForgeRegistries.Keys.BIOME_MODIFIERS, modifierMap));
         */
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

    public static void registerSpawnPlacements() {
        SpawnPlacements.register(CACEntities.OTTER.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OtterEntity::checkOtterSpawnRules);
        SpawnPlacements.register(CACEntities.KOI_FISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(CACEntities.DRAGONFLY.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, DragonflyEntity::checkDragonflySpawnRules);
        SpawnPlacements.register(CACEntities.SEA_BUNNY.get(), SpawnPlacements.Type.create("ON_WATER_GROUND",
                ((levelReader, blockPos, entityType) -> levelReader.getFluidState(blockPos).is(FluidTags.WATER) && levelReader.getBlockState(blockPos.below()).isFaceSturdy(levelReader, blockPos.below(), Direction.UP))), Heightmap.Types.OCEAN_FLOOR, SeaBunnyEntity::checkSeaBunnySpawnRules);
        SpawnPlacements.register(CACEntities.FERRET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacements.register(CACEntities.DUMBO_OCTOPUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DumboOctopusEntity::checkDumboOctopusSpawnRules);
        SpawnPlacements.register(CACEntities.LEAF_INSECT.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, LeafInsectEntity::checkLeafInsectSpawnRules);
        SpawnPlacements.register(CACEntities.RED_PANDA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    }
}
