package io.github.bonsaistudi0s.crittersandcompanions.common.handler;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;

public class AttributeHandler {
    
    public static void registerAttributes() {
        EntityAttributeRegistry.register(CACEntities.OTTER, OtterEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.JUMPING_SPIDER, JumpingSpiderEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.KOI_FISH, KoiFishEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.DRAGONFLY, DragonflyEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.SEA_BUNNY, SeaBunnyEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.SHIMA_ENAGA, ShimaEnagaEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.FERRET, FerretEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.DUMBO_OCTOPUS, DumboOctopusEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.LEAF_INSECT, LeafInsectEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.RED_PANDA, RedPandaEntity::createAttributes);

        EntityAttributeRegistry.register(CACEntities.LADYBUG, LadybugEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.STAG_BEETLE, StagBeetleEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.ROLY_POLY, RolyPolyEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.SNAIL, SnailEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.STICK_BUG, StickBugEntity::createAttributes);
        EntityAttributeRegistry.register(CACEntities.WEEVIL, WeevilEntity::createAttributes);
    }
}
