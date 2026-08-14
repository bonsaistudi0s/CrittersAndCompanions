package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import org.jetbrains.annotations.NotNull;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;

public class BabySpeedModifierBehaviour implements Behaviour {

    private static final ResourceLocation BABY_SPEED_ID = CrittersAndCompanions.createId("baby_speed_modifier");
    @NotNull
    private final AgeableMob owner;
    private final double speedMultiplier;

    public BabySpeedModifierBehaviour(@NotNull AgeableMob owner, double speedMultiplier) {
        this.owner = owner;
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public void setAge(int age) {
        var speedAttribute = owner.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttribute == null) {
            return;
        }

        if (owner.isBaby()) {
            if (!speedAttribute.hasModifier(BABY_SPEED_ID)) {
                speedAttribute.addPermanentModifier(new AttributeModifier(
                        BABY_SPEED_ID,
                        speedMultiplier,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ));
            }
        } else {
            if (speedAttribute.hasModifier(BABY_SPEED_ID)) {
                speedAttribute.removeModifier(BABY_SPEED_ID);
            }
        }
    }
}
