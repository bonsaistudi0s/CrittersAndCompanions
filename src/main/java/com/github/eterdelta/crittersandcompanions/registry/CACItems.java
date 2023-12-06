package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.item.DragonflyArmorItem;
import com.github.eterdelta.crittersandcompanions.item.GrapplingHookItem;
import com.github.eterdelta.crittersandcompanions.item.PearlNecklaceItem;
import com.github.eterdelta.crittersandcompanions.item.SilkLeashItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CACItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CrittersAndCompanions.MODID);

    public static final RegistryObject<Item> CLAM = ITEMS.register("clam", () -> new Item(new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> DRAGONFLY_WING = ITEMS.register("dragonfly_wing", () -> new Item(new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> KOI_FISH = ITEMS.register("koi_fish", () -> new Item(new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB).food(Foods.TROPICAL_FISH)));
    public static final RegistryObject<Item> PEARL = ITEMS.register("pearl", () -> new Item(new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> SILK = ITEMS.register("silk", () -> new Item(new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB)));

    public static final RegistryObject<Item> SEA_BUNNY_SLIME_BOTTLE = ITEMS.register("sea_bunny_slime_bottle", () -> new Item(new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
    public static final RegistryObject<Item> SEA_BUNNY_SLIME_BLOCK = ITEMS.register("sea_bunny_slime_block", () -> new BlockItem(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get(), new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB)));

    public static final RegistryObject<Item> SILK_LEAD = ITEMS.register("silk_lead", () -> new SilkLeashItem(new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> GRAPPLING_HOOK = ITEMS.register("grappling_hook", () -> new GrapplingHookItem(new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB).stacksTo(1)));

    public static final RegistryObject<Item> PEARL_NECKLACE_1 = ITEMS.register("pearl_necklace_1", () -> new PearlNecklaceItem(new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB).stacksTo(1), 1));
    public static final RegistryObject<Item> PEARL_NECKLACE_2 = ITEMS.register("pearl_necklace_2", () -> new PearlNecklaceItem(new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB).stacksTo(1), 2));
    public static final RegistryObject<Item> PEARL_NECKLACE_3 = ITEMS.register("pearl_necklace_3", () -> new PearlNecklaceItem(new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB).stacksTo(1), 3));

    public static final RegistryObject<Item> DUMBO_OCTOPUS_BUCKET = ITEMS.register("dumbo_octopus_bucket", () -> new MobBucketItem(CACEntities.DUMBO_OCTOPUS, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> KOI_FISH_BUCKET = ITEMS.register("koi_fish_bucket", () -> new MobBucketItem(CACEntities.KOI_FISH, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> SEA_BUNNY_BUCKET = ITEMS.register("sea_bunny_bucket", () -> new MobBucketItem(CACEntities.SEA_BUNNY, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1).tab(CrittersAndCompanions.CREATIVE_TAB)));

    public static final RegistryObject<Item> DIAMOND_DRAGONFLY_ARMOR = ITEMS.register("diamond_dragonfly_armor", () -> new DragonflyArmorItem(16, "diamond", (new Item.Properties()).stacksTo(1).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> GOLD_DRAGONFLY_ARMOR = ITEMS.register("gold_dragonfly_armor", () -> new DragonflyArmorItem(8, "gold", (new Item.Properties()).stacksTo(1).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> IRON_DRAGONFLY_ARMOR = ITEMS.register("iron_dragonfly_armor", () -> new DragonflyArmorItem(4, "iron", (new Item.Properties()).stacksTo(1).tab(CrittersAndCompanions.CREATIVE_TAB)));

    public static final RegistryObject<Item> DRAGONFLY_SPAWN_EGG = ITEMS.register("dragonfly_spawn_egg", () -> new ForgeSpawnEggItem(CACEntities.DRAGONFLY, 0x08EECF, 0xD3FF96, (new Item.Properties()).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> FERRET_SPAWN_EGG = ITEMS.register("ferret_spawn_egg", () -> new ForgeSpawnEggItem(CACEntities.FERRET, 0xC5AC88, 0x37212D, (new Item.Properties()).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> DUMBO_OCTOPUS_SPAWN_EGG = ITEMS.register("dumbo_octopus_spawn_egg", () -> new ForgeSpawnEggItem(CACEntities.DUMBO_OCTOPUS, 0xFCDC4C, 0x162630, (new Item.Properties()).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> JUMPING_SPIDER_SPAWN_EGG = ITEMS.register("jumping_spider_spawn_egg", () -> new ForgeSpawnEggItem(CACEntities.JUMPING_SPIDER, 0x34191E, 0x865F33, (new Item.Properties()).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> KOI_FISH_SPAWN_EGG = ITEMS.register("koi_fish_spawn_egg", () -> new ForgeSpawnEggItem(CACEntities.KOI_FISH, 0xF3ECED, 0xFB5321, (new Item.Properties()).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> LEAF_INSECT_SPAWN_EGG = ITEMS.register("leaf_insect_spawn_egg", () -> new ForgeSpawnEggItem(CACEntities.LEAF_INSECT, 0xDAD475, 0x3C6C34, (new Item.Properties()).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> OTTER_SPAWN_EGG = ITEMS.register("otter_spawn_egg", () -> new ForgeSpawnEggItem(CACEntities.OTTER, 0x352C34, 0xB49494, (new Item.Properties()).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> RED_PANDA_SPAWN_EGG = ITEMS.register("red_panda_spawn_egg", () -> new ForgeSpawnEggItem(CACEntities.RED_PANDA, 0xF4943C, 0x13131B, (new Item.Properties()).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> SEA_BUNNY_SPAWN_EGG = ITEMS.register("sea_bunny_spawn_egg", () -> new ForgeSpawnEggItem(CACEntities.SEA_BUNNY, 0xF4ECE4, 0x453337, (new Item.Properties()).tab(CrittersAndCompanions.CREATIVE_TAB)));
    public static final RegistryObject<Item> SHIMA_ENAGA_SPAWN_EGG = ITEMS.register("shima_enaga_spawn_egg", () -> new ForgeSpawnEggItem(CACEntities.SHIMA_ENAGA, 0xFCFCEC, 0x5C3C34, (new Item.Properties()).tab(CrittersAndCompanions.CREATIVE_TAB)));

    public static final RegistryObject<Item> SILK_COCOON = ITEMS.register("silk_cocoon", () -> new BlockItem(CACBlocks.SILK_COCOON.get(), new Item.Properties().tab(CrittersAndCompanions.CREATIVE_TAB)));
}