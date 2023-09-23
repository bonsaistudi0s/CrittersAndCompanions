package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CACEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CrittersAndCompanions.MODID);

    public static final RegistryObject<EntityType<DragonflyEntity>> DRAGONFLY = ENTITIES.register("dragonfly", () -> EntityType.Builder.of(DragonflyEntity::new, MobCategory.AMBIENT).sized(0.8F, 0.25F).build("dragonfly"));
    public static final RegistryObject<EntityType<DumboOctopusEntity>> DUMBO_OCTOPUS = ENTITIES.register("dumbo_octopus", () -> EntityType.Builder.of(DumboOctopusEntity::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.4F).build("dumbo_octopus"));
    public static final RegistryObject<EntityType<FerretEntity>> FERRET = ENTITIES.register("ferret", () -> EntityType.Builder.of(FerretEntity::new, MobCategory.CREATURE).sized(0.75F, 0.65F).build("ferret"));
    public static final RegistryObject<EntityType<GrapplingHookEntity>> GRAPPLING_HOOK = ENTITIES.register("grappling_hook", () -> EntityType.Builder.<GrapplingHookEntity>of(GrapplingHookEntity::new, MobCategory.MISC).sized(0.2F, 0.2F).build("grappling_hook"));
    public static final RegistryObject<EntityType<JumpingSpiderEntity>> JUMPING_SPIDER = ENTITIES.register("jumping_spider", () -> EntityType.Builder.of(JumpingSpiderEntity::new, MobCategory.CREATURE).sized(0.5F, 0.4F).build("jumping_spider"));
    public static final RegistryObject<EntityType<KoiFishEntity>> KOI_FISH = ENTITIES.register("koi_fish", () -> EntityType.Builder.of(KoiFishEntity::new, MobCategory.WATER_AMBIENT).sized(0.6F, 0.3F).build("koi_fish"));
    public static final RegistryObject<EntityType<LeafInsectEntity>> LEAF_INSECT = ENTITIES.register("leaf_insect", () -> EntityType.Builder.of(LeafInsectEntity::new, MobCategory.CREATURE).sized(0.4F, 0.3F).build("leaf_insect"));
    public static final RegistryObject<EntityType<OtterEntity>> OTTER = ENTITIES.register("otter", () -> EntityType.Builder.of(OtterEntity::new, MobCategory.WATER_CREATURE).sized(0.8F, 0.4F).build("otter"));
    public static final RegistryObject<EntityType<RedPandaEntity>> RED_PANDA = ENTITIES.register("red_panda", () -> EntityType.Builder.of(RedPandaEntity::new, MobCategory.CREATURE).sized(0.65F, 0.55F).build("red_panda"));
    public static final RegistryObject<EntityType<SeaBunnyEntity>> SEA_BUNNY = ENTITIES.register("sea_bunny", () -> EntityType.Builder.of(SeaBunnyEntity::new, MobCategory.WATER_AMBIENT).sized(0.45F, 0.3F).build("sea_bunny"));
    public static final RegistryObject<EntityType<ShimaEnagaEntity>> SHIMA_ENAGA = ENTITIES.register("shima_enaga", () -> EntityType.Builder.of(ShimaEnagaEntity::new, MobCategory.CREATURE).sized(0.5F, 0.6F).build("shima_enaga"));
}