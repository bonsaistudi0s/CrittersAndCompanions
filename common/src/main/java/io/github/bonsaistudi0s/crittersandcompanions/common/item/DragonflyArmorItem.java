package io.github.bonsaistudi0s.crittersandcompanions.common.item;

import com.google.common.base.Suppliers;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.function.Supplier;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;

public class DragonflyArmorItem extends Item {

    private final ResourceLocation texture;
    private final Supplier<ItemAttributeModifiers> attributes;

    public DragonflyArmorItem(Holder<ArmorMaterial> material, String tierName, Properties properties) {
        this(material, CrittersAndCompanions.createId("textures/entity/dragonfly_armor_" + tierName + ".png"), properties);
    }

    public DragonflyArmorItem(Holder<ArmorMaterial> material, ResourceLocation tierName, Properties properties) {
        super(properties);
        this.texture = tierName;
        this.attributes = Suppliers.memoize(() ->
            ItemAttributeModifiers.builder()
                    .add(Attributes.ARMOR, new AttributeModifier(CrittersAndCompanions.createId("armor"), material.value().getDefense(ArmorItem.Type.CHESTPLATE), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.BODY)
                    .add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(CrittersAndCompanions.createId("toughness"), material.value().toughness(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.BODY)
                    .build()
        );
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return attributes.get();
    }

}
