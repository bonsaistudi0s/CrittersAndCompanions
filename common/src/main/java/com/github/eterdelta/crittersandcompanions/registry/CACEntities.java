package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import com.github.eterdelta.crittersandcompanions.entity.DumboOctopusEntity;
import com.github.eterdelta.crittersandcompanions.entity.FerretEntity;
import com.github.eterdelta.crittersandcompanions.entity.GrapplingHookEntity;
import com.github.eterdelta.crittersandcompanions.entity.JumpingSpiderEntity;
import com.github.eterdelta.crittersandcompanions.entity.KoiFishEntity;
import com.github.eterdelta.crittersandcompanions.entity.LadybugEntity;
import com.github.eterdelta.crittersandcompanions.entity.LeafInsectEntity;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import com.github.eterdelta.crittersandcompanions.entity.RedPandaEntity;
import com.github.eterdelta.crittersandcompanions.entity.SeaBunnyEntity;
import com.github.eterdelta.crittersandcompanions.entity.ShimaEnagaEntity;
import com.github.eterdelta.crittersandcompanions.entity.StabBeetleEntity;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import com.github.eterdelta.crittersandcompanions.platform.RegistryHelper;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class CACEntities {
    private static final RegistryHelper<EntityType<?>> ENTITIES = Services.PLATFORM.createRegistryHelper(Registries.ENTITY_TYPE, CrittersAndCompanions.MODID);

    public static final RegistryEntry<EntityType<DragonflyEntity>> DRAGONFLY = register("dragonfly", () -> EntityType.Builder.of(DragonflyEntity::new, MobCategory.AMBIENT).sized(0.9F, 0.4F));
    public static final RegistryEntry<EntityType<DumboOctopusEntity>> DUMBO_OCTOPUS = register("dumbo_octopus", () -> EntityType.Builder.of(DumboOctopusEntity::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.4F));
    public static final RegistryEntry<EntityType<FerretEntity>> FERRET = register("ferret", () -> EntityType.Builder.of(FerretEntity::new, MobCategory.CREATURE).sized(0.8F, 0.7F));
    public static final RegistryEntry<EntityType<GrapplingHookEntity>> GRAPPLING_HOOK = register("grappling_hook", () -> EntityType.Builder.<GrapplingHookEntity>of(GrapplingHookEntity::new, MobCategory.MISC).sized(0.2F, 0.2F).noSave().noSummon());
    public static final RegistryEntry<EntityType<JumpingSpiderEntity>> JUMPING_SPIDER = register("jumping_spider", () -> EntityType.Builder.of(JumpingSpiderEntity::new, MobCategory.CREATURE).sized(0.5F, 0.4F));
    public static final RegistryEntry<EntityType<KoiFishEntity>> KOI_FISH = register("koi_fish", () -> EntityType.Builder.of(KoiFishEntity::new, MobCategory.WATER_AMBIENT).sized(0.6F, 0.3F));
    public static final RegistryEntry<EntityType<LeafInsectEntity>> LEAF_INSECT = register("leaf_insect", () -> EntityType.Builder.of(LeafInsectEntity::new, MobCategory.AMBIENT).sized(0.4F, 0.3F));
    public static final RegistryEntry<EntityType<OtterEntity>> OTTER = register("otter", () -> EntityType.Builder.of(OtterEntity::new, MobCategory.WATER_CREATURE).sized(0.8F, 0.6F));
    public static final RegistryEntry<EntityType<RedPandaEntity>> RED_PANDA = register("red_panda", () -> EntityType.Builder.of(RedPandaEntity::new, MobCategory.CREATURE).sized(0.75F, 0.65F));
    public static final RegistryEntry<EntityType<SeaBunnyEntity>> SEA_BUNNY = register("sea_bunny", () -> EntityType.Builder.of(SeaBunnyEntity::new, MobCategory.WATER_AMBIENT).sized(0.45F, 0.3F));
    public static final RegistryEntry<EntityType<ShimaEnagaEntity>> SHIMA_ENAGA = register("shima_enaga", () -> EntityType.Builder.of(ShimaEnagaEntity::new, MobCategory.CREATURE).sized(0.5F, 0.6F));

    public static final RegistryEntry<EntityType<LadybugEntity>> LADYBUG = register("ladybug", () -> EntityType.Builder.of(LadybugEntity::new, MobCategory.AMBIENT).sized(0.5F, 0.6F));
    public static final RegistryEntry<EntityType<StabBeetleEntity>> STAG_BEETLE = register("stag_beetle", () -> EntityType.Builder.of(StabBeetleEntity::new, MobCategory.AMBIENT).sized(0.5F, 0.6F));

    private static <T extends Entity> RegistryEntry<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> factory) {
        return ENTITIES.register(name, () -> factory.get().build(name));
    }

    public static void init() {
        // Load the class
    }
}