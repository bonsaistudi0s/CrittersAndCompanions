package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.item.DragonflyArmorItem;
import com.github.eterdelta.crittersandcompanions.item.GrapplingHookItem;
import com.github.eterdelta.crittersandcompanions.item.PearlNecklaceItem;
import com.github.eterdelta.crittersandcompanions.item.SilkLeashItem;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import com.github.eterdelta.crittersandcompanions.platform.RegistryHelper;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;

public class CACItems {
    private static final RegistryHelper<Item> ITEMS = Services.PLATFORM.createRegistryHelper(Registries.ITEM, CrittersAndCompanions.MODID);

    public static final RegistryEntry<Item> CLAM = ITEMS.register("clam", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> DRAGONFLY_WING = ITEMS.register("dragonfly_wing", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> KOI_FISH = ITEMS.register("koi_fish", () -> new Item(new Item.Properties().food(Foods.TROPICAL_FISH)));
    public static final RegistryEntry<Item> PEARL = ITEMS.register("pearl", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> SILK = ITEMS.register("silk", () -> new Item(new Item.Properties()));

    public static final RegistryEntry<Item> SEA_BUNNY_SLIME_BOTTLE = ITEMS.register("sea_bunny_slime_bottle", () -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
    public static final RegistryEntry<Item> SEA_BUNNY_SLIME_BLOCK = ITEMS.register("sea_bunny_slime_block", () -> new BlockItem(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get(), new Item.Properties()));

    public static final RegistryEntry<Item> SILK_LEAD = ITEMS.register("silk_lead", () -> new SilkLeashItem(new Item.Properties()));
    public static final RegistryEntry<Item> GRAPPLING_HOOK = ITEMS.register("grappling_hook", () -> new GrapplingHookItem(new Item.Properties().stacksTo(1)));

    public static final RegistryEntry<Item> PEARL_NECKLACE_1 = ITEMS.register("pearl_necklace_1", () -> new PearlNecklaceItem(new Item.Properties().stacksTo(1), 1));
    public static final RegistryEntry<Item> PEARL_NECKLACE_2 = ITEMS.register("pearl_necklace_2", () -> new PearlNecklaceItem(new Item.Properties().stacksTo(1), 2));
    public static final RegistryEntry<Item> PEARL_NECKLACE_3 = ITEMS.register("pearl_necklace_3", () -> new PearlNecklaceItem(new Item.Properties().stacksTo(1), 3));

    public static final RegistryEntry<Item> DUMBO_OCTOPUS_BUCKET = ITEMS.register("dumbo_octopus_bucket", () -> new MobBucketItem(CACEntities.DUMBO_OCTOPUS.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)));
    public static final RegistryEntry<Item> KOI_FISH_BUCKET = ITEMS.register("koi_fish_bucket", () -> new MobBucketItem(CACEntities.KOI_FISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)));
    public static final RegistryEntry<Item> SEA_BUNNY_BUCKET = ITEMS.register("sea_bunny_bucket", () -> new MobBucketItem(CACEntities.SEA_BUNNY.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)));

    public static final RegistryEntry<Item> DIAMOND_DRAGONFLY_ARMOR = ITEMS.register("diamond_dragonfly_armor", () -> new DragonflyArmorItem(ArmorMaterials.DIAMOND, "diamond", (new Item.Properties()).stacksTo(1)));
    public static final RegistryEntry<Item> GOLD_DRAGONFLY_ARMOR = ITEMS.register("gold_dragonfly_armor", () -> new DragonflyArmorItem(ArmorMaterials.GOLD, "gold", (new Item.Properties()).stacksTo(1)));
    public static final RegistryEntry<Item> IRON_DRAGONFLY_ARMOR = ITEMS.register("iron_dragonfly_armor", () -> new DragonflyArmorItem(ArmorMaterials.IRON, "iron", (new Item.Properties()).stacksTo(1)));

    public static final RegistryEntry<Item> DRAGONFLY_SPAWN_EGG = registerSpawnEgg(CACEntities.DRAGONFLY, 0x08EECF, 0xD3FF96);
    public static final RegistryEntry<Item> FERRET_SPAWN_EGG = registerSpawnEgg(CACEntities.FERRET, 0xC5AC88, 0x37212D);
    public static final RegistryEntry<Item> DUMBO_OCTOPUS_SPAWN_EGG = registerSpawnEgg(CACEntities.DUMBO_OCTOPUS, 0xFCDC4C, 0x162630);
    public static final RegistryEntry<Item> JUMPING_SPIDER_SPAWN_EGG = registerSpawnEgg(CACEntities.JUMPING_SPIDER, 0x34191E, 0x865F33);
    public static final RegistryEntry<Item> KOI_FISH_SPAWN_EGG = registerSpawnEgg(CACEntities.KOI_FISH, 0xF3ECED, 0xFB5321);
    public static final RegistryEntry<Item> LEAF_INSECT_SPAWN_EGG = registerSpawnEgg(CACEntities.LEAF_INSECT, 0xDAD475, 0x3C6C34);
    public static final RegistryEntry<Item> OTTER_SPAWN_EGG = registerSpawnEgg(CACEntities.OTTER, 0x352C34, 0xB49494);
    public static final RegistryEntry<Item> RED_PANDA_SPAWN_EGG = registerSpawnEgg(CACEntities.RED_PANDA, 0xF4943C, 0x13131B);
    public static final RegistryEntry<Item> SEA_BUNNY_SPAWN_EGG = registerSpawnEgg(CACEntities.SEA_BUNNY, 0xF4ECE4, 0x453337);
    public static final RegistryEntry<Item> SHIMA_ENAGA_SPAWN_EGG = registerSpawnEgg(CACEntities.SHIMA_ENAGA, 0xFCFCEC, 0x5C3C34);
    public static final RegistryEntry<Item> LADYBUG_SPAWN_EGG = registerSpawnEgg(CACEntities.LADYBUG, 0xDE3023, 0x170302);
    public static final RegistryEntry<Item> STAG_BEETLE_SPAWN_EGG = registerSpawnEgg(CACEntities.STAG_BEETLE, 0x11111a, 0x2b4854);
    public static final RegistryEntry<Item> ROLLYPOLLY_SPAWN_EGG = registerSpawnEgg(CACEntities.ROLLYPOLLY, 0x918780, 0x4e494d);

    public static final RegistryEntry<Item> SILK_COCOON = ITEMS.register("silk_cocoon", () -> new BlockItem(CACBlocks.SILK_COCOON.get(), new Item.Properties()));

    private static <T extends Mob> RegistryEntry<Item> registerSpawnEgg(RegistryEntry<EntityType<T>> entity, int primary, int secondary) {
        return ITEMS.register(entity.getKey().location().getPath() + "_spawn_egg", () -> Services.PLATFORM.createSpawnEgg(entity, primary, secondary, new Item.Properties()));
    }

    public static void init() {
        // Load the class
    }

}