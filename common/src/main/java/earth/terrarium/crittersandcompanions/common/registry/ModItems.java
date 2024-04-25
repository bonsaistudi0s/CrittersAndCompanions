package earth.terrarium.crittersandcompanions.common.registry;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.item.DragonflyArmorItem;
import earth.terrarium.crittersandcompanions.common.item.GrapplingHookItem;
import earth.terrarium.crittersandcompanions.common.item.PearlNecklaceItem;
import earth.terrarium.crittersandcompanions.common.item.SilkLeashItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class ModItems {
    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, CrittersAndCompanions.MODID);
    public static final ResourcefulRegistry<CreativeModeTab> TABS = ResourcefulRegistries.create(BuiltInRegistries.CREATIVE_MODE_TAB, CrittersAndCompanions.MODID);

    public static final Supplier<Item> CLAM = ITEMS.register("clam", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> DRAGONFLY_WING = ITEMS.register("dragonfly_wing", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> KOI_FISH = ITEMS.register("koi_fish", () -> new Item(new Item.Properties().food(Foods.TROPICAL_FISH)));
    public static final Supplier<Item> PEARL = ITEMS.register("pearl", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> SILK = ITEMS.register("silk", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> SEA_BUNNY_SLIME_BOTTLE = ITEMS.register("sea_bunny_slime_bottle", () -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
    public static final Supplier<Item> SEA_BUNNY_SLIME_BLOCK = ITEMS.register("sea_bunny_slime_block", () -> new BlockItem(ModBlocks.SEA_BUNNY_SLIME_BLOCK.get(), new Item.Properties()));

    public static final Supplier<Item> SILK_LEAD = ITEMS.register("silk_lead", () -> new SilkLeashItem(new Item.Properties()));
    public static final Supplier<Item> GRAPPLING_HOOK = ITEMS.register("grappling_hook", () -> new GrapplingHookItem(new Item.Properties().stacksTo(1)));

    public static final Supplier<Item> PEARL_NECKLACE_1 = ITEMS.register("pearl_necklace_1", () -> new PearlNecklaceItem(new Item.Properties().stacksTo(1), 1));
    public static final Supplier<Item> PEARL_NECKLACE_2 = ITEMS.register("pearl_necklace_2", () -> new PearlNecklaceItem(new Item.Properties().stacksTo(1), 2));
    public static final Supplier<Item> PEARL_NECKLACE_3 = ITEMS.register("pearl_necklace_3", () -> new PearlNecklaceItem(new Item.Properties().stacksTo(1), 3));

    /*
    public static final Supplier<Item> DUMBO_OCTOPUS_BUCKET = ITEMS.register("dumbo_octopus_bucket", createMobBucketItem(ModEntities.DUMBO_OCTOPUS, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
    public static final Supplier<Item> KOI_FISH_BUCKET = ITEMS.register("koi_fish_bucket", createMobBucketItem(ModEntities.KOI_FISH, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
    public static final Supplier<Item> SEA_BUNNY_BUCKET = ITEMS.register("sea_bunny_bucket", createMobBucketItem(ModEntities.SEA_BUNNY, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
     */
    
    public static final Supplier<Item> DIAMOND_DRAGONFLY_ARMOR = ITEMS.register("diamond_dragonfly_armor", () -> new DragonflyArmorItem(16, "diamond", (new Item.Properties()).stacksTo(1)));
    public static final Supplier<Item> GOLD_DRAGONFLY_ARMOR = ITEMS.register("gold_dragonfly_armor", () -> new DragonflyArmorItem(8, "gold", (new Item.Properties()).stacksTo(1)));
    public static final Supplier<Item> IRON_DRAGONFLY_ARMOR = ITEMS.register("iron_dragonfly_armor", () -> new DragonflyArmorItem(4, "iron", (new Item.Properties()).stacksTo(1)));

    /*
    public static final Supplier<Item> DRAGONFLY_SPAWN_EGG = ITEMS.register("dragonfly_spawn_egg", createSpawnEggItem(ModEntities.DRAGONFLY, 0x08EECF, 0xD3FF96, (new Item.Properties())));
    public static final Supplier<Item> FERRET_SPAWN_EGG = ITEMS.register("ferret_spawn_egg", createSpawnEggItem(ModEntities.FERRET, 0xC5AC88, 0x37212D, (new Item.Properties())));
    public static final Supplier<Item> DUMBO_OCTOPUS_SPAWN_EGG = ITEMS.register("dumbo_octopus_spawn_egg", createSpawnEggItem(ModEntities.DUMBO_OCTOPUS, 0xFCDC4C, 0x162630, (new Item.Properties())));
    public static final Supplier<Item> JUMPING_SPIDER_SPAWN_EGG = ITEMS.register("jumping_spider_spawn_egg", createSpawnEggItem(ModEntities.JUMPING_SPIDER, 0x34191E, 0x865F33, (new Item.Properties())));
    public static final Supplier<Item> KOI_FISH_SPAWN_EGG = ITEMS.register("koi_fish_spawn_egg", createSpawnEggItem(ModEntities.KOI_FISH, 0xF3ECED, 0xFB5321, (new Item.Properties())));
    public static final Supplier<Item> LEAF_INSECT_SPAWN_EGG = ITEMS.register("leaf_insect_spawn_egg", createSpawnEggItem(ModEntities.LEAF_INSECT, 0xDAD475, 0x3C6C34, (new Item.Properties())));
    public static final Supplier<Item> OTTER_SPAWN_EGG = ITEMS.register("otter_spawn_egg", createSpawnEggItem(ModEntities.OTTER, 0x352C34, 0xB49494, (new Item.Properties())));
    public static final Supplier<Item> RED_PANDA_SPAWN_EGG = ITEMS.register("red_panda_spawn_egg", createSpawnEggItem(ModEntities.RED_PANDA, 0xF4943C, 0x13131B, (new Item.Properties())));
    public static final Supplier<Item> SEA_BUNNY_SPAWN_EGG = ITEMS.register("sea_bunny_spawn_egg", createSpawnEggItem(ModEntities.SEA_BUNNY, 0xF4ECE4, 0x453337, (new Item.Properties())));
    public static final Supplier<Item> SHIMA_ENAGA_SPAWN_EGG = ITEMS.register("shima_enaga_spawn_egg", createSpawnEggItem(ModEntities.SHIMA_ENAGA, 0xFCFCEC, 0x5C3C34, (new Item.Properties())));
     */

    public static final Supplier<Item> SILK_COCOON = ITEMS.register("silk_cocoon", () -> new BlockItem(ModBlocks.SILK_COCOON.get(), new Item.Properties()));

    public static final Supplier<CreativeModeTab> TAB = TABS.register("main", () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup.crittersandcompanions")).icon(() -> PEARL_NECKLACE_1.get().getDefaultInstance()).build());

    @ExpectPlatform
    public static Supplier<Item> createSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, Item.Properties properties) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static Supplier<Item> createMobBucketItem(Supplier<? extends EntityType<? extends Mob>> type, Fluid fluid, SoundEvent sound, Item.Properties properties) {
        throw new NotImplementedException();
    }
}