package io.github.bonsaistudi0s.crittersandcompanions;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.UseOnContext;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.bonsaistudi0s.crittersandcompanions.common.api.CACColors;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.handler.LushCaveSpawnHandler;
import io.github.bonsaistudi0s.crittersandcompanions.common.handler.PlayerHandler;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.CACPacketHandler;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.*;
import io.github.bonsaistudi0s.crittersandcompanions.common.world.CACWorldGen;

public class CrittersAndCompanions {

    public static final String MODID = "crittersandcompanions";

    public static ResourceLocation createId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("main", () ->
            CreativeTabRegistry.create(
                    Component.translatable("itemGroup." + MODID),
                    () -> CACItems.PEARL_NECKLACE_1.get().getDefaultInstance()
            )
    );

    public static void init() {
        CREATIVE_TABS.register();

        CACArmorMaterials.init();
        CACBlocks.init();
        CACEntities.init();
        CACItems.init();
        CACMenuTypes.init();
        CACPotions.init();
        CACSounds.init();
        CACPacketHandler.registerPackets();

        registerColors();

        LifecycleEvent.SETUP.register(() -> {
            CACEntities.setup();
            CACWorldGen.register();
            CACItems.registerCompostables();
        });

        CACCommonConfig.HANDLER.load();

        registerEvents();
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

    private static void registerEvents() {
        TickEvent.PLAYER_POST.register(PlayerHandler::onPlayerTick);
        TickEvent.SERVER_POST.register(LushCaveSpawnHandler::tick);

        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            if (player.level().isClientSide()) {
                return EventResult.pass();
            }

            var result = PlayerHandler.onPlayerEntityInteract(entity, new UseOnContext(player, hand, null));
            if (result != null) {
                return EventResult.interruptFalse();
            }

            return EventResult.pass();
        });
    }
}
