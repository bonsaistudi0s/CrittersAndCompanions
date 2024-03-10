package earth.terrarium.crittersandcompanions.common.handler;

import earth.terrarium.crittersandcompanions.common.entity.*;
import earth.terrarium.crittersandcompanions.common.registry.CACEntities;
import earth.terrarium.crittersandcompanions.common.registry.CACItems;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Arrays;

public class SpawnHandler {

    public static void onLivingCheckSpawn(LivingEntity entity, boolean isNaturalSpawn, MobSpawnType spawnReason) {
        if (entity instanceof Drowned drowned && spawnReason == MobSpawnType.NATURAL && drowned.getRandom().nextFloat() <= 0.05F) {
            drowned.setItemInHand(InteractionHand.OFF_HAND, CACItems.CLAM.get().getDefaultInstance());
        }
    }

    @SafeVarargs
    public static HolderSet<Biome> getBiomeHolderSet(Registry<Biome> registry, ResourceKey<Biome>... biomes) {
        return HolderSet.direct(Arrays.stream(biomes).map(registry::getHolderOrThrow).toList());
    }

    public static void registerSpawnPlacements() {
        SpawnPlacements.register(CACEntities.OTTER.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OtterEntity::checkOtterSpawnRules);
        SpawnPlacements.register(CACEntities.KOI_FISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(CACEntities.DRAGONFLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, DragonflyEntity::checkDragonflySpawnRules);
        SpawnPlacements.register(CACEntities.SEA_BUNNY.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, SeaBunnyEntity::checkSeaBunnySpawnRules);
        SpawnPlacements.register(CACEntities.FERRET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacements.register(CACEntities.DUMBO_OCTOPUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DumboOctopusEntity::checkDumboOctopusSpawnRules);
        SpawnPlacements.register(CACEntities.LEAF_INSECT.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, LeafInsectEntity::checkLeafInsectSpawnRules);
        SpawnPlacements.register(CACEntities.RED_PANDA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    }
}
