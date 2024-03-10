package earth.terrarium.crittersandcompanions.common.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.entity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class ModEntities {
    public static final ResourcefulRegistry<EntityType<?>> ENTITIES = ResourcefulRegistries.create(BuiltInRegistries.ENTITY_TYPE, CrittersAndCompanions.MODID);

    public static final Supplier<EntityType<DragonflyEntity>> DRAGONFLY = ENTITIES.register("dragonfly", () -> EntityType.Builder.of(DragonflyEntity::new, MobCategory.AMBIENT).sized(0.8F, 0.25F).build("dragonfly"));
    public static final Supplier<EntityType<DumboOctopusEntity>> DUMBO_OCTOPUS = ENTITIES.register("dumbo_octopus", () -> EntityType.Builder.of(DumboOctopusEntity::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.4F).build("dumbo_octopus"));
    public static final Supplier<EntityType<FerretEntity>> FERRET = ENTITIES.register("ferret", () -> EntityType.Builder.of(FerretEntity::new, MobCategory.CREATURE).sized(0.75F, 0.65F).build("ferret"));
    public static final Supplier<EntityType<GrapplingHookEntity>> GRAPPLING_HOOK = ENTITIES.register("grappling_hook", () -> EntityType.Builder.<GrapplingHookEntity>of(GrapplingHookEntity::new, MobCategory.MISC).sized(0.2F, 0.2F).build("grappling_hook"));
    public static final Supplier<EntityType<JumpingSpiderEntity>> JUMPING_SPIDER = ENTITIES.register("jumping_spider", () -> EntityType.Builder.of(JumpingSpiderEntity::new, MobCategory.CREATURE).sized(0.5F, 0.4F).build("jumping_spider"));
    public static final Supplier<EntityType<KoiFishEntity>> KOI_FISH = ENTITIES.register("koi_fish", () -> EntityType.Builder.of(KoiFishEntity::new, MobCategory.WATER_AMBIENT).sized(0.6F, 0.3F).build("koi_fish"));
    public static final Supplier<EntityType<LeafInsectEntity>> LEAF_INSECT = ENTITIES.register("leaf_insect", () -> EntityType.Builder.of(LeafInsectEntity::new, MobCategory.CREATURE).sized(0.4F, 0.3F).build("leaf_insect"));
    public static final Supplier<EntityType<OtterEntity>> OTTER = ENTITIES.register("otter", () -> EntityType.Builder.of(OtterEntity::new, MobCategory.WATER_CREATURE).sized(0.8F, 0.4F).build("otter"));
    public static final Supplier<EntityType<RedPandaEntity>> RED_PANDA = ENTITIES.register("red_panda", () -> EntityType.Builder.of(RedPandaEntity::new, MobCategory.CREATURE).sized(0.65F, 0.55F).build("red_panda"));
    public static final Supplier<EntityType<SeaBunnyEntity>> SEA_BUNNY = ENTITIES.register("sea_bunny", () -> EntityType.Builder.of(SeaBunnyEntity::new, MobCategory.WATER_AMBIENT).sized(0.45F, 0.3F).build("sea_bunny"));
    public static final Supplier<EntityType<ShimaEnagaEntity>> SHIMA_ENAGA = ENTITIES.register("shima_enaga", () -> EntityType.Builder.of(ShimaEnagaEntity::new, MobCategory.CREATURE).sized(0.5F, 0.6F).build("shima_enaga"));
}