package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.api.CACColors;
import com.github.eterdelta.crittersandcompanions.entity.*;
import com.github.eterdelta.crittersandcompanions.handler.SpawnHandler;
import com.github.eterdelta.crittersandcompanions.network.CACPacketHandler;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import com.github.eterdelta.crittersandcompanions.platform.RegistryHelper;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import com.github.eterdelta.crittersandcompanions.registry.CACBlocks;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import java.util.function.BiConsumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;

public class CrittersAndCompanions {
    public static final String MODID = "crittersandcompanions";

    public static ResourceLocation createId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private static final RegistryHelper<CreativeModeTab> CREATIVE_TABS = Services.PLATFORM.createRegistryHelper(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryEntry<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> CACItems.PEARL_NECKLACE_1.get().getDefaultInstance())
            .title(Component.translatable("itemGroup." + MODID))
            .build()
    );

    public static void init() {
        CACBlocks.init();
        CACEntities.init();
        CACItems.init();
        CACSounds.init();
        CACPacketHandler.registerPackets();
        registerColors();
    }

    public static void setup() {
        SpawnHandler.registerSpawnPlacements();
    }

    public static void onAttributeCreation(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> event) {
        event.accept(CACEntities.OTTER.get(), OtterEntity.createAttributes().build());
        event.accept(CACEntities.JUMPING_SPIDER.get(), JumpingSpiderEntity.createAttributes().build());
        event.accept(CACEntities.KOI_FISH.get(), KoiFishEntity.createAttributes().build());
        event.accept(CACEntities.DRAGONFLY.get(), DragonflyEntity.createAttributes().build());
        event.accept(CACEntities.SEA_BUNNY.get(), SeaBunnyEntity.createAttributes().build());
        event.accept(CACEntities.SHIMA_ENAGA.get(), ShimaEnagaEntity.createAttributes().build());
        event.accept(CACEntities.FERRET.get(), FerretEntity.createAttributes().build());
        event.accept(CACEntities.DUMBO_OCTOPUS.get(), DumboOctopusEntity.createAttributes().build());
        event.accept(CACEntities.LEAF_INSECT.get(), LeafInsectEntity.createAttributes().build());
        event.accept(CACEntities.RED_PANDA.get(), RedPandaEntity.createAttributes().build());

        event.accept(CACEntities.LADYBUG.get(), LadybugEntity.createAttributes().build());
        event.accept(CACEntities.STAG_BEETLE.get(), StagBeetleEntity.createAttributes().build());
        event.accept(CACEntities.ROLLYPOLLY.get(), RollypollyEntity.createAttributes().build());
        event.accept(CACEntities.SNAIL.get(), SnailEntity.createAttributes().build());
        event.accept(CACEntities.STICK_BUG.get(), StickBugEntity.createAttributes().build());
        event.accept(CACEntities.WEEVIL.get(), WeevilEntity.createAttributes().build());
    }

    private static void registerColors() {
        CACColors.register(DyeColor.WHITE);
        CACColors.register(DyeColor.ORANGE);
        CACColors.register(DyeColor.MAGENTA);
        CACColors.register(DyeColor.LIGHT_BLUE);
        CACColors.register(DyeColor.YELLOW);
        CACColors.register(DyeColor.LIME);
        CACColors.register(DyeColor.PINK);
        CACColors.register(DyeColor.GRAY);
        CACColors.register(DyeColor.LIGHT_GRAY);
        CACColors.register(DyeColor.CYAN);
        CACColors.register(DyeColor.PURPLE);
        CACColors.register(DyeColor.BLUE);
        CACColors.register(DyeColor.BROWN);
        CACColors.register(DyeColor.GREEN);
        CACColors.register(DyeColor.RED);
        CACColors.register(DyeColor.BLACK);
    }

}
