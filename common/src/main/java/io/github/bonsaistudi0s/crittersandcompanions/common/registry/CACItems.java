package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import dev.architectury.core.item.ArchitecturyMobBucketItem;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.item.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class CACItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(CrittersAndCompanions.MODID, Registries.ITEM);

    private static Item.Properties baseProperties() {
        return new Item.Properties().arch$tab(CrittersAndCompanions.CREATIVE_TAB);
    }

    public static final RegistrySupplier<Item> CLAM = ITEMS.register("clam", () -> new Item(baseProperties()));
    public static final RegistrySupplier<Item> DRAGONFLY_WING = ITEMS.register("dragonfly_wing", () -> new Item(baseProperties()));
    public static final RegistrySupplier<Item> KOI_FISH = ITEMS.register("koi_fish", () -> new Item(baseProperties().food(Foods.TROPICAL_FISH)));
    public static final RegistrySupplier<Item> PEARL = ITEMS.register("pearl", () -> new Item(baseProperties()));
    public static final RegistrySupplier<Item> SILK = ITEMS.register("silk", () -> new Item(baseProperties()));

    public static final RegistrySupplier<Item> SEA_BUNNY_SLIME_BOTTLE = ITEMS.register("sea_bunny_slime_bottle", () -> new Item(baseProperties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));
    public static final RegistrySupplier<Item> SEA_BUNNY_SLIME_BLOCK = ITEMS.register("sea_bunny_slime_block", () -> new BlockItem(CACBlocks.SEA_BUNNY_SLIME_BLOCK.get(), baseProperties()));

    public static final RegistrySupplier<Item> SILK_LEAD = ITEMS.register("silk_lead", () -> new SilkLeashItem(baseProperties()));
    public static final RegistrySupplier<Item> GRAPPLING_HOOK = ITEMS.register("grappling_hook", () -> {
        var properties = baseProperties().stacksTo(1);

        if (CACCommonConfig.HANDLER.instance().grapplingHook.enableDurability) {
            properties = properties.durability(128);
        }

        return new GrapplingHookItem(properties);
    });

    public static final RegistrySupplier<Item> PEARL_NECKLACE_1 = ITEMS.register("pearl_necklace_1", () -> new PearlNecklaceItem(baseProperties().stacksTo(1), 1));
    public static final RegistrySupplier<Item> PEARL_NECKLACE_2 = ITEMS.register("pearl_necklace_2", () -> new PearlNecklaceItem(baseProperties().stacksTo(1), 2));
    public static final RegistrySupplier<Item> PEARL_NECKLACE_3 = ITEMS.register("pearl_necklace_3", () -> new PearlNecklaceItem(baseProperties().stacksTo(1), 3));

    public static final RegistrySupplier<Item> DUMBO_OCTOPUS_BUCKET = ITEMS.register("dumbo_octopus_bucket", () -> new ArchitecturyMobBucketItem(CACEntities.DUMBO_OCTOPUS, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, baseProperties().stacksTo(1)));
    public static final RegistrySupplier<Item> KOI_FISH_BUCKET = ITEMS.register("koi_fish_bucket", () -> new ArchitecturyMobBucketItem(CACEntities.KOI_FISH, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, baseProperties().stacksTo(1)));
    public static final RegistrySupplier<Item> SEA_BUNNY_BUCKET = ITEMS.register("sea_bunny_bucket", () -> new ArchitecturyMobBucketItem(CACEntities.SEA_BUNNY, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, baseProperties().stacksTo(1)));

    public static final RegistrySupplier<Item> DIAMOND_DRAGONFLY_ARMOR = ITEMS.register("diamond_dragonfly_armor", () -> new DragonflyArmorItem(ArmorMaterials.DIAMOND, "diamond", baseProperties().stacksTo(1)));
    public static final RegistrySupplier<Item> GOLD_DRAGONFLY_ARMOR = ITEMS.register("gold_dragonfly_armor", () -> new DragonflyArmorItem(ArmorMaterials.GOLD, "gold", baseProperties().stacksTo(1)));
    public static final RegistrySupplier<Item> IRON_DRAGONFLY_ARMOR = ITEMS.register("iron_dragonfly_armor", () -> new DragonflyArmorItem(ArmorMaterials.IRON, "iron", baseProperties().stacksTo(1)));

    public static final RegistrySupplier<Item> DRAGONFLY_SPAWN_EGG = registerSpawnEgg(CACEntities.DRAGONFLY, 0x08EECF, 0xD3FF96);
    public static final RegistrySupplier<Item> FERRET_SPAWN_EGG = registerSpawnEgg(CACEntities.FERRET, 0xC5AC88, 0x37212D);
    public static final RegistrySupplier<Item> DUMBO_OCTOPUS_SPAWN_EGG = registerSpawnEgg(CACEntities.DUMBO_OCTOPUS, 0xFCDC4C, 0x162630);
    public static final RegistrySupplier<Item> JUMPING_SPIDER_SPAWN_EGG = registerSpawnEgg(CACEntities.JUMPING_SPIDER, 0x34191E, 0x865F33);
    public static final RegistrySupplier<Item> KOI_FISH_SPAWN_EGG = registerSpawnEgg(CACEntities.KOI_FISH, 0xF3ECED, 0xFB5321);
    public static final RegistrySupplier<Item> LEAF_INSECT_SPAWN_EGG = registerSpawnEgg(CACEntities.LEAF_INSECT, 0xDAD475, 0x3C6C34);
    public static final RegistrySupplier<Item> OTTER_SPAWN_EGG = registerSpawnEgg(CACEntities.OTTER, 0x352C34, 0xB49494);
    public static final RegistrySupplier<Item> RED_PANDA_SPAWN_EGG = registerSpawnEgg(CACEntities.RED_PANDA, 0xF4943C, 0x13131B);
    public static final RegistrySupplier<Item> SEA_BUNNY_SPAWN_EGG = registerSpawnEgg(CACEntities.SEA_BUNNY, 0xF4ECE4, 0x453337);
    public static final RegistrySupplier<Item> SHIMA_ENAGA_SPAWN_EGG = registerSpawnEgg(CACEntities.SHIMA_ENAGA, 0xFCFCEC, 0x5C3C34);
    public static final RegistrySupplier<Item> LADYBUG_SPAWN_EGG = registerSpawnEgg(CACEntities.LADYBUG, 0xDE3023, 0x170302);
    public static final RegistrySupplier<Item> STAG_BEETLE_SPAWN_EGG = registerSpawnEgg(CACEntities.STAG_BEETLE, 0x11111a, 0x2b4854);
    public static final RegistrySupplier<Item> ROLY_POLY_SPAWN_EGG = registerSpawnEgg(CACEntities.ROLY_POLY, 0x918780, 0x4e494d);
    public static final RegistrySupplier<Item> SNAIL_SPAWN_EGG = registerSpawnEgg(CACEntities.SNAIL, 0x834434, 0xeb9b5c);
    public static final RegistrySupplier<Item> STICK_BUG_SPAWN_EGG = registerSpawnEgg(CACEntities.STICK_BUG, 0xa97338, 0x70851e);
    public static final RegistrySupplier<Item> WEEVIL_SPAWN_EGG = registerSpawnEgg(CACEntities.WEEVIL, 0xb06b3a, 0x975632);

    public static final RegistrySupplier<Item> SILK_COCOON = ITEMS.register("silk_cocoon", () -> new BlockItem(CACBlocks.SILK_COCOON.get(), baseProperties()));

    public static final RegistrySupplier<Item> ACORN = ITEMS.register("acorn", () -> new AcornItem(baseProperties()));
    public static final RegistrySupplier<Item> ACORN_HAT = ITEMS.register("acorn_hat", getAcornHatItem(baseProperties()));

    public static final RegistrySupplier<Item> SNAIL_SLIME_BOTTLE = ITEMS.register("snail_slime_bottle", () -> new SnailSlimeBottleItem(baseProperties().craftRemainder(Items.GLASS_BOTTLE).stacksTo(16)));

    private static <T extends Mob> RegistrySupplier<Item> registerSpawnEgg(RegistrySupplier<EntityType<T>> entity, int primary, int secondary) {
        return ITEMS.register(entity.getKey().location().getPath() + "_spawn_egg", () ->
                new ArchitecturySpawnEggItem(
                        entity,
                        primary,
                        secondary,
                        baseProperties()
                )
        );
    }

    public static void init() {
        ITEMS.register();
    }

    @ExpectPlatform
    private static Supplier<? extends Item> getAcornHatItem(Item.Properties properties) {
        throw new AssertionError();
    }
}