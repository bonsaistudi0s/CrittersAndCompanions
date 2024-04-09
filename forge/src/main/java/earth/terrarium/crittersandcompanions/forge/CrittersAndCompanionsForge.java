package earth.terrarium.crittersandcompanions.forge;

import com.mojang.serialization.Codec;
import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.handler.AnimalHandler;
import earth.terrarium.crittersandcompanions.common.handler.PlayerHandler;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import earth.terrarium.crittersandcompanions.datagen.server.SpawnData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.GeckoLib;

import java.util.function.Supplier;

import static earth.terrarium.crittersandcompanions.CrittersAndCompanions.MODID;

@Mod(MODID)
public class CrittersAndCompanionsForge {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_TABLES = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

    public static final RegistryObject<Codec<FishingModifier>> FISHING_MODIFIER = LOOT_TABLES.register("fish", () -> FishingModifier.CODEC);
    public static final RegistryObject<Codec<DrowndModifier>> DROWNED_MODIFIER = LOOT_TABLES.register("drowned", () -> DrowndModifier.CODEC);

    public CrittersAndCompanionsForge() {
        GeckoLib.initialize();
        CrittersAndCompanions.init();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        LOOT_TABLES.register(eventBus);

        eventBus.addListener(this::onSetup);
        eventBus.addListener(this::onAttributeCreation);
        eventBus.addListener(this::addItemsToTab);

        forgeBus.addListener(this::onPlayerTick);
        forgeBus.addListener(this::onPlayerEntityInteract);
        forgeBus.addListener(this::startTrackingPlayer);
        forgeBus.addListener(this::stopTrackingPlayer);

        eventBus.addListener(this::gatherData);
    }

    public void gatherData(GatherDataEvent event) {
        SpawnData.datagenBiomeModifiers(event);
    }

    public void onSetup(FMLCommonSetupEvent event) {
        AnimalHandler.registerSpawnPlacements();
    }

    private void onAttributeCreation(EntityAttributeCreationEvent event) {
        AnimalHandler.onAttributeCreation(event::put);
    }

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PlayerHandler.onPlayerTick(event.player);
        }
    }

    public void onPlayerEntityInteract(PlayerInteractEvent.EntityInteract event) {
        PlayerHandler.onPlayerEntityInteract(event.getEntity(), event.getHand(), event.getTarget(), event::setCanceled, event::setCancellationResult);
    }

    public void startTrackingPlayer(PlayerEvent.StartTracking event) {
        PlayerHandler.onPlayerStartTracking(event.getEntity(), event.getTarget());
    }

    public void stopTrackingPlayer(PlayerEvent.StopTracking event) {
        PlayerHandler.onPlayerStopTracking(event.getEntity(), event.getTarget());
    }

    public void addItemsToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ModItems.TAB.get()) {
            ModItems.ITEMS.getEntries().stream().map(Supplier::get).forEach(event::accept);
        }
    }
}
