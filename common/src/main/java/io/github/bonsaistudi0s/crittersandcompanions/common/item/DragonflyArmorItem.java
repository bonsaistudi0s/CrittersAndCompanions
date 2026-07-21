package io.github.bonsaistudi0s.crittersandcompanions.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DragonflyArmorItem extends Item {

    private static final UUID ARMOR_UUID = UUID.fromString("23a017ef-23f2-4e6c-ab8e-8e8d881519ab");
    private static final UUID TOUGHNESS_UUID = UUID.fromString("001f5774-a240-4bbb-903b-f63ebef4076a");

    private final ResourceLocation texture;
    private final Multimap<Attribute, AttributeModifier> attributes;

    public DragonflyArmorItem(ArmorMaterial material, String tierName, Item.Properties properties) {
        this(material, CrittersAndCompanions.createId("textures/entity/dragonfly_armor_" + tierName + ".png"), properties);
    }

    public DragonflyArmorItem(ArmorMaterial material, ResourceLocation tierName, Item.Properties properties) {
        super(properties);
        this.texture = tierName;
        this.attributes = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ARMOR, new AttributeModifier(ARMOR_UUID, "armor", material.getDefenseForType(ArmorItem.Type.CHESTPLATE), AttributeModifier.Operation.ADDITION))
                .put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(TOUGHNESS_UUID, "toughness", material.getToughness(), AttributeModifier.Operation.ADDITION))
                .build();
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.CHEST) return attributes;
        return ImmutableMultimap.of();
    }
}
