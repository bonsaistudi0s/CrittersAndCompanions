package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.projectiles.MudBallProjectile;

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

    private static <T extends Entity> RegistrySupplier<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> factory) {
        return ENTITIES.register(name, () -> factory.get().build(name));
    }

    public static void init() {
        ENTITIES.register();
    }
}