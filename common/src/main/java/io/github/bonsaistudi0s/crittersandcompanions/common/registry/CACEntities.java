package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACSpawnConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.base.AgeableWaterAnimal;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.projectiles.MudBallProjectile;
import io.github.bonsaistudi0s.crittersandcompanions.common.handler.LushCaveSpawnHandler;

public class CACEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(CrittersAndCompanions.MODID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<DragonflyEntity>> DRAGONFLY = register("dragonfly", () -> EntityType.Builder.of(DragonflyEntity::new, MobCategory.AMBIENT).sized(0.9F, 0.4F));
    public static final RegistrySupplier<EntityType<DumboOctopusEntity>> DUMBO_OCTOPUS = register("dumbo_octopus", () -> EntityType.Builder.of(DumboOctopusEntity::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.4F));
    public static final RegistrySupplier<EntityType<FerretEntity>> FERRET = register("ferret", () -> EntityType.Builder.of(FerretEntity::new, MobCategory.CREATURE).sized(0.8F, 0.7F));
    public static final RegistrySupplier<EntityType<JumpingSpiderEntity>> JUMPING_SPIDER = register("jumping_spider", () -> EntityType.Builder.of(JumpingSpiderEntity::new, MobCategory.AMBIENT).sized(0.5F, 0.4F));
    public static final RegistrySupplier<EntityType<KoiFishEntity>> KOI_FISH = register("koi_fish", () -> EntityType.Builder.of(KoiFishEntity::new, MobCategory.WATER_AMBIENT).sized(0.6F, 0.3F));
    public static final RegistrySupplier<EntityType<LeafInsectEntity>> LEAF_INSECT = register("leaf_insect", () -> EntityType.Builder.of(LeafInsectEntity::new, MobCategory.AMBIENT).sized(0.4F, 0.3F));
    public static final RegistrySupplier<EntityType<OtterEntity>> OTTER = register("otter", () -> EntityType.Builder.of(OtterEntity::new, MobCategory.WATER_CREATURE).sized(0.8F, 0.6F));
    public static final RegistrySupplier<EntityType<RedPandaEntity>> RED_PANDA = register("red_panda", () -> EntityType.Builder.of(RedPandaEntity::new, MobCategory.CREATURE).sized(0.75F, 0.65F));
    public static final RegistrySupplier<EntityType<SeaBunnyEntity>> SEA_BUNNY = register("sea_bunny", () -> EntityType.Builder.of(SeaBunnyEntity::new, MobCategory.WATER_AMBIENT).sized(0.45F, 0.3F));
    public static final RegistrySupplier<EntityType<ShimaEnagaEntity>> SHIMA_ENAGA = register("shima_enaga", () -> EntityType.Builder.of(ShimaEnagaEntity::new, MobCategory.CREATURE).sized(0.5F, 0.6F));

    public static final RegistrySupplier<EntityType<LadybugEntity>> LADYBUG = register("ladybug", () -> EntityType.Builder.of(LadybugEntity::new, MobCategory.AMBIENT).sized(0.5F, 0.375F));
    public static final RegistrySupplier<EntityType<StagBeetleEntity>> STAG_BEETLE = register("stag_beetle", () -> EntityType.Builder.of(StagBeetleEntity::new, MobCategory.AMBIENT).sized(0.5F, 0.45F));
    public static final RegistrySupplier<EntityType<RolyPolyEntity>> ROLY_POLY = register("roly_poly", () -> EntityType.Builder.of(RolyPolyEntity::new, MobCategory.AMBIENT).sized(0.5F, 0.4F));
    public static final RegistrySupplier<EntityType<SnailEntity>> SNAIL = register("snail", () -> EntityType.Builder.of(SnailEntity::new, MobCategory.AMBIENT).sized(0.5F, 0.575F));
    public static final RegistrySupplier<EntityType<StickBugEntity>> STICK_BUG = register("stick_bug", () -> EntityType.Builder.of(StickBugEntity::new, MobCategory.AMBIENT).sized(0.5F, 0.385F));
    public static final RegistrySupplier<EntityType<WeevilEntity>> WEEVIL = register("weevil", () -> EntityType.Builder.of(WeevilEntity::new, MobCategory.AMBIENT).sized(0.5F, 0.5F));

    public static final RegistrySupplier<EntityType<GrapplingHookEntity>> GRAPPLING_HOOK = register("grappling_hook", () -> EntityType.Builder.<GrapplingHookEntity>of(GrapplingHookEntity::new, MobCategory.MISC).sized(0.2F, 0.2F).noSave().noSummon());
    public static final RegistrySupplier<EntityType<MudBallProjectile>> MUD_BALL = register("mud_ball", () -> EntityType.Builder.<MudBallProjectile>of(MudBallProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));

    private static final Logger LOGGER = LoggerFactory.getLogger(CACEntities.class);

    private static <T extends Entity> RegistrySupplier<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> factory) {
        return ENTITIES.register(name, () -> factory.get().build(name));
    }

    public static void init() {
        ENTITIES.register();
        registerAttributes();
        registerSpawnPlacements();
    }

    public static void setup() {
        registerSpawns();
    }

    private static void registerSpawns() {
        var spawningConfig = CACCommonConfig.HANDLER.instance().spawning;

        for (var entityEntry : spawningConfig.getAllEntries().entrySet()) {
            var entityPath = entityEntry.getKey();
            var type = CACSpawnConfig.resolveEntityType(entityPath);
            if (type == null) {
                LOGGER.warn("Unknown entity '{}' in spawn config, skipping", entityPath);
                continue;
            }

            for (var spawn : entityEntry.getValue()) {
                if (spawn.weight() <= 0) {
                    continue;
                }

                var spawnerData = new MobSpawnSettings.SpawnerData(type, spawn.weight(), spawn.min(), spawn.max());

                if (spawn.isTag()) {
                    var tag = TagKey.create(Registries.BIOME, spawn.biomeLocation());
                    BiomeModifications.addProperties(context -> context.hasTag(tag), (context, properties) -> properties.getSpawnProperties().addSpawn(type.getCategory(), spawnerData));
                } else {
                    if (spawn.biomeLocation().equals(Biomes.LUSH_CAVES.location())) {
                        LushCaveSpawnHandler.addSpawnerData(spawnerData);
                        continue;
                    }

                    BiomeModifications.addProperties(context -> context.getKey().isPresent() && context.getKey().get().equals(spawn.biomeLocation()), (context, properties) -> properties.getSpawnProperties().addSpawn(type.getCategory(), spawnerData));
                }
            }
        }
    }

    private static void registerSpawnPlacements() {
        SpawnPlacementsRegistry.register(CACEntities.OTTER, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OtterEntity::checkOtterSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.KOI_FISH, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.DRAGONFLY, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, DragonflyEntity::checkDragonflySpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.SEA_BUNNY, SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, AgeableWaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.FERRET, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.DUMBO_OCTOPUS, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.LEAF_INSECT, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, LeafInsectEntity::checkLeafInsectSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.RED_PANDA, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.SHIMA_ENAGA, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Animal::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.JUMPING_SPIDER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (e, l, s, p, r) -> true);
        SpawnPlacementsRegistry.register(CACEntities.LADYBUG, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CACEntities::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.STAG_BEETLE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CACEntities::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.ROLY_POLY, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CACEntities::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.SNAIL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CACEntities::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.STICK_BUG, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CACEntities::checkBugSpawnRules);
        SpawnPlacementsRegistry.register(CACEntities.WEEVIL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CACEntities::checkBugSpawnRules);
    }

    private static boolean checkBugSpawnRules(EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        var belowState = level.getBlockState(pos.below());
        var isValidFloor = belowState.is(CACTags.BUG_SPAWN_GROUND);

        var isLushCave = level.getBiome(pos).is(Biomes.LUSH_CAVES);
        if (isLushCave) {
            if (level.getBrightness(LightLayer.BLOCK, pos) == 0) {
                return false;
            }

            return isValidFloor || belowState.is(BlockTags.BASE_STONE_OVERWORLD);
        }

        return isValidFloor && level.getRawBrightness(pos, 0) >= 5;
    }

    private static void registerAttributes() {
        EntityAttributeRegistry.register(OTTER, OtterEntity::createAttributes);
        EntityAttributeRegistry.register(JUMPING_SPIDER, JumpingSpiderEntity::createAttributes);
        EntityAttributeRegistry.register(KOI_FISH, KoiFishEntity::createAttributes);
        EntityAttributeRegistry.register(DRAGONFLY, DragonflyEntity::createAttributes);
        EntityAttributeRegistry.register(SEA_BUNNY, SeaBunnyEntity::createAttributes);
        EntityAttributeRegistry.register(SHIMA_ENAGA, ShimaEnagaEntity::createAttributes);
        EntityAttributeRegistry.register(FERRET, FerretEntity::createAttributes);
        EntityAttributeRegistry.register(DUMBO_OCTOPUS, DumboOctopusEntity::createAttributes);
        EntityAttributeRegistry.register(LEAF_INSECT, LeafInsectEntity::createAttributes);
        EntityAttributeRegistry.register(RED_PANDA, RedPandaEntity::createAttributes);
        EntityAttributeRegistry.register(LADYBUG, LadybugEntity::createAttributes);
        EntityAttributeRegistry.register(STAG_BEETLE, StagBeetleEntity::createAttributes);
        EntityAttributeRegistry.register(ROLY_POLY, RolyPolyEntity::createAttributes);
        EntityAttributeRegistry.register(SNAIL, SnailEntity::createAttributes);
        EntityAttributeRegistry.register(STICK_BUG, StickBugEntity::createAttributes);
        EntityAttributeRegistry.register(WEEVIL, WeevilEntity::createAttributes);
    }
}