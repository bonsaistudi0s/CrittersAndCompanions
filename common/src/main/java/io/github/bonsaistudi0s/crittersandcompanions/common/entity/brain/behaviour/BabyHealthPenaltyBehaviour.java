package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import org.jetbrains.annotations.NotNull;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;

public class BabyHealthPenaltyBehaviour implements Behaviour {

    private static final ResourceLocation BABY_HEALTH_ID = CrittersAndCompanions.createId("baby_health_penalty");
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
                var baseValue = healthAttribute.getBaseValue();
                var targetHealth = Math.ceil((baseValue * 0.5) / 2.0) * 2.0;
                var penalty = targetHealth - baseValue;

                healthAttribute.addPermanentModifier(new AttributeModifier(
                        BABY_HEALTH_ID,
                        penalty,
                        AttributeModifier.Operation.ADD_VALUE
                ));

                owner.setHealth(Math.min(owner.getHealth(), owner.getMaxHealth()));
            }
        } else {
            if (healthAttribute.hasModifier(BABY_HEALTH_ID)) {
                var modifier = healthAttribute.getModifier(BABY_HEALTH_ID);
                var amount = modifier != null ? modifier.amount() : 0.0;
                healthAttribute.removeModifier(BABY_HEALTH_ID);
                if (amount < 0) {
                    owner.heal((float) -amount);
                }
            }
        }
    }
}
