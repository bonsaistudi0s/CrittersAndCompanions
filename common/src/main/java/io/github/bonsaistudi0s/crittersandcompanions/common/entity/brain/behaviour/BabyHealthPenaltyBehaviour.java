package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class BabyHealthPenaltyBehaviour implements Behaviour {

    private static final UUID BABY_HEALTH_ID = UUID.fromString("6174a7eb-6d0e-4361-b1e1-e1cb67c7e5a0");

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

        for (var modifier : new ArrayList<>(healthAttribute.getModifiers())) {
            if ("baby_health_penalty".equals(modifier.getName()) && !BABY_HEALTH_ID.equals(modifier.getId())) {
                healthAttribute.removeModifier(modifier.getId());
                if (!owner.isBaby()) {
                    owner.heal((float) (healthAttribute.getBaseValue() * 0.5));
                }
            }
        }

        if (owner.isBaby()) {
            if (healthAttribute.getModifier(BABY_HEALTH_ID) == null) {
                var baseValue = healthAttribute.getBaseValue();
                var targetHealth = Math.ceil((baseValue * 0.5) / 2.0) * 2.0;
                var penalty = targetHealth - baseValue;

                healthAttribute.addPermanentModifier(new AttributeModifier(
                        BABY_HEALTH_ID,
                        "baby_health_penalty",
                        penalty,
                        AttributeModifier.Operation.ADDITION
                ));

                owner.setHealth(Math.min(owner.getHealth(), owner.getMaxHealth()));
            }
        } else {
            var modifier = healthAttribute.getModifier(BABY_HEALTH_ID);
            if (modifier != null) {
                var amount = modifier.getAmount();
                healthAttribute.removeModifier(BABY_HEALTH_ID);
                if (amount < 0) {
                    owner.heal((float) -amount);
                }
            }
        }
    }
}
