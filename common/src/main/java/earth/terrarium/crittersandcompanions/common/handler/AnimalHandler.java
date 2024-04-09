package earth.terrarium.crittersandcompanions.common.handler;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.crittersandcompanions.common.entity.*;
import earth.terrarium.crittersandcompanions.common.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.BiConsumer;

public class AnimalHandler {

    @ExpectPlatform
    public static boolean onAnimalTame(TamableAnimal animal, Player player) {
        throw new NotImplementedException("AnimalHandler#onAnimalTame(TamableAnimal, Player) is not implemented");
    }

    @ExpectPlatform
    public static Attribute getSwimSpeedAttribute() {
        throw new NotImplementedException("AnimalHandler#getSwimSpeedAttribute() is not implemented");
    }

    public static void onAttributeCreation(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> event) {
        event.accept(ModEntities.OTTER.get(), OtterEntity.createAttributes().build());
        event.accept(ModEntities.JUMPING_SPIDER.get(), JumpingSpiderEntity.createAttributes().build());
        event.accept(ModEntities.KOI_FISH.get(), KoiFishEntity.createAttributes().build());
        event.accept(ModEntities.DRAGONFLY.get(), DragonflyEntity.createAttributes().build());
        event.accept(ModEntities.SEA_BUNNY.get(), SeaBunnyEntity.createAttributes().build());
        event.accept(ModEntities.SHIMA_ENAGA.get(), ShimaEnagaEntity.createAttributes().build());
        event.accept(ModEntities.FERRET.get(), FerretEntity.createAttributes().build());
        event.accept(ModEntities.DUMBO_OCTOPUS.get(), DumboOctopusEntity.createAttributes().build());
        event.accept(ModEntities.LEAF_INSECT.get(), LeafInsectEntity.createAttributes().build());
        event.accept(ModEntities.RED_PANDA.get(), RedPandaEntity.createAttributes().build());
    }

    public static void registerSpawnPlacements() {
        SpawnPlacements.register(ModEntities.OTTER.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OtterEntity::checkOtterSpawnRules);
        SpawnPlacements.register(ModEntities.KOI_FISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WaterAnimal::checkSurfaceWaterAnimalSpawnRules);
        SpawnPlacements.register(ModEntities.DRAGONFLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, DragonflyEntity::checkDragonflySpawnRules);
        SpawnPlacements.register(ModEntities.SEA_BUNNY.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.OCEAN_FLOOR, SeaBunnyEntity::checkSeaBunnySpawnRules);
        SpawnPlacements.register(ModEntities.FERRET.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        SpawnPlacements.register(ModEntities.DUMBO_OCTOPUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DumboOctopusEntity::checkDumboOctopusSpawnRules);
        SpawnPlacements.register(ModEntities.LEAF_INSECT.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, LeafInsectEntity::checkLeafInsectSpawnRules);
        SpawnPlacements.register(ModEntities.RED_PANDA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
    }
}
