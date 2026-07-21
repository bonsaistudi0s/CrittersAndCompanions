package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum CACArmorMaterials implements ArmorMaterial {
    ACORN(
            "acorn",
            5,
            15,
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.HELMET, 1);
                map.put(ArmorItem.Type.CHESTPLATE, 0);
                map.put(ArmorItem.Type.LEGGINGS, 0);
                map.put(ArmorItem.Type.BOOTS, 0);
            }),
            SoundEvents.ARMOR_EQUIP_LEATHER,
            () -> Ingredient.of(CACItems.ACORN.get()),
            0.0F,
            0.0F
    );

    private static final EnumMap<ArmorItem.Type, Integer> BASE_DURABILITY = Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.HELMET, 11);
    });

    private final String name;
    private final int durabilityMultiplier;
    private final int enchantmentValue;
    private final EnumMap<ArmorItem.Type, Integer> defenseForType;
    private final SoundEvent sound;
    private final Supplier<Ingredient> repairIngredient;
    private final float toughness;
    private final float knockbackResistance;

    CACArmorMaterials(String name, int durabilityMultiplier, int enchantmentValue, EnumMap<ArmorItem.Type, Integer> defenseForType, SoundEvent sound, Supplier<Ingredient> repairIngredient, float toughness, float knockbackResistance) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.enchantmentValue = enchantmentValue;
        this.defenseForType = defenseForType;
        this.sound = sound;
        this.repairIngredient = repairIngredient;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return BASE_DURABILITY.get(type) * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.defenseForType.get(type);
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public @NotNull SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public @NotNull String getName() {
        return CrittersAndCompanions.MODID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
