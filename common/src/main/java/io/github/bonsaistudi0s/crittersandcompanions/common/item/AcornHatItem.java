package io.github.bonsaistudi0s.crittersandcompanions.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACArmorMaterials;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;

import java.util.UUID;

public abstract class AcornHatItem extends ArmorItem implements GeoItem {

    private static final UUID ACORN_HAT_HEALTH_UUID = UUID.fromString("fdde18a4-90a8-4fc4-8525-d0c67d3e6284");

    public AcornHatItem(Properties properties) {
        super(
                CACArmorMaterials.ACORN,
                Type.HELMET,
                properties
        );
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        var superModifiers = super.getDefaultAttributeModifiers(slot);

        if (slot == EquipmentSlot.HEAD) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            builder.putAll(superModifiers);

            builder.put(
                    Attributes.MAX_HEALTH,
                    new AttributeModifier(
                            ACORN_HAT_HEALTH_UUID,
                            "Acorn hat health",
                            4.0,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }

        return superModifiers;
    }
}
