package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import org.jetbrains.annotations.NotNull;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;

public class BabyHealthPenaltyBehaviour implements Behaviour {

    private static final ResourceLocation BABY_HEALTH_ID = CrittersAndCompanions.createId("baby_health_penalty");
    private static final AttributeModifier BABY_HEALTH_MODIFIER = new AttributeModifier(
            BABY_HEALTH_ID,
            -0.5D,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    );

    @NotNull
    private final AgeableMob owner;

    public BabyHealthPenaltyBehaviour(@NotNull AgeableMob owner) {
        this.owner = owner;
    }

    @Override
    public void setAge(int age) {
        var healthAttribute = owner.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttribute == null) {
            return;
        }

        if (owner.isBaby()) {
            if (!healthAttribute.hasModifier(BABY_HEALTH_ID)) {
                healthAttribute.addPermanentModifier(BABY_HEALTH_MODIFIER);
                owner.setHealth(Math.min(owner.getHealth(), owner.getMaxHealth()));
            }
        } else {
            if (healthAttribute.hasModifier(BABY_HEALTH_ID)) {
                healthAttribute.removeModifier(BABY_HEALTH_ID);
                owner.heal(owner.getMaxHealth() / 2.0F);
            }
        }
    }
}
