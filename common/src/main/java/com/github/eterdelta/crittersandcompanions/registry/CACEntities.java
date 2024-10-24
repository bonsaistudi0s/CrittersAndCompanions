package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import com.github.eterdelta.crittersandcompanions.entity.DumboOctopusEntity;
import com.github.eterdelta.crittersandcompanions.entity.FerretEntity;
import com.github.eterdelta.crittersandcompanions.entity.GrapplingHookEntity;
import com.github.eterdelta.crittersandcompanions.entity.JumpingSpiderEntity;
import com.github.eterdelta.crittersandcompanions.entity.KoiFishEntity;
import com.github.eterdelta.crittersandcompanions.entity.LeafInsectEntity;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import com.github.eterdelta.crittersandcompanions.entity.RedPandaEntity;
import com.github.eterdelta.crittersandcompanions.entity.SeaBunnyEntity;
import com.github.eterdelta.crittersandcompanions.entity.ShimaEnagaEntity;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import com.github.eterdelta.crittersandcompanions.platform.RegistryHelper;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class CACEntities {
    private static final RegistryHelper<EntityType<?>> ENTITIES = Services.PLATFORM.createRegistryHelper(Registries.ENTITY_TYPE, CrittersAndCompanions.MODID);

    public static final RegistryEntry<EntityType<DragonflyEntity>> DRAGONFLY = ENTITIES.register("dragonfly", () -> EntityType.Builder.of(DragonflyEntity::new, MobCategory.AMBIENT).sized(0.9F, 0.4F).build("dragonfly"));
    public static final RegistryEntry<EntityType<DumboOctopusEntity>> DUMBO_OCTOPUS = ENTITIES.register("dumbo_octopus", () -> EntityType.Builder.of(DumboOctopusEntity::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.4F).build("dumbo_octopus"));
    public static final RegistryEntry<EntityType<FerretEntity>> FERRET = ENTITIES.register("ferret", () -> EntityType.Builder.of(FerretEntity::new, MobCategory.CREATURE).sized(0.75F, 0.65F).build("ferret"));
    public static final RegistryEntry<EntityType<GrapplingHookEntity>> GRAPPLING_HOOK = ENTITIES.register("grappling_hook", () -> EntityType.Builder.<GrapplingHookEntity>of(GrapplingHookEntity::new, MobCategory.MISC).sized(0.2F, 0.2F).noSave().noSummon().build("grappling_hook"));
    public static final RegistryEntry<EntityType<JumpingSpiderEntity>> JUMPING_SPIDER = ENTITIES.register("jumping_spider", () -> EntityType.Builder.of(JumpingSpiderEntity::new, MobCategory.CREATURE).sized(0.5F, 0.4F).build("jumping_spider"));
    public static final RegistryEntry<EntityType<KoiFishEntity>> KOI_FISH = ENTITIES.register("koi_fish", () -> EntityType.Builder.of(KoiFishEntity::new, MobCategory.WATER_AMBIENT).sized(0.6F, 0.3F).build("koi_fish"));
    public static final RegistryEntry<EntityType<LeafInsectEntity>> LEAF_INSECT = ENTITIES.register("leaf_insect", () -> EntityType.Builder.of(LeafInsectEntity::new, MobCategory.AMBIENT).sized(0.4F, 0.3F).build("leaf_insect"));
    public static final RegistryEntry<EntityType<OtterEntity>> OTTER = ENTITIES.register("otter", () -> EntityType.Builder.of(OtterEntity::new, MobCategory.WATER_CREATURE).sized(0.8F, 0.4F).build("otter"));
    public static final RegistryEntry<EntityType<RedPandaEntity>> RED_PANDA = ENTITIES.register("red_panda", () -> EntityType.Builder.of(RedPandaEntity::new, MobCategory.CREATURE).sized(0.65F, 0.55F).build("red_panda"));
    public static final RegistryEntry<EntityType<SeaBunnyEntity>> SEA_BUNNY = ENTITIES.register("sea_bunny", () -> EntityType.Builder.of(SeaBunnyEntity::new, MobCategory.WATER_AMBIENT).sized(0.45F, 0.3F).build("sea_bunny"));
    public static final RegistryEntry<EntityType<ShimaEnagaEntity>> SHIMA_ENAGA = ENTITIES.register("shima_enaga", () -> EntityType.Builder.of(ShimaEnagaEntity::new, MobCategory.CREATURE).sized(0.5F, 0.6F).build("shima_enaga"));

    public static void init() {
        // Load the class
    }
}