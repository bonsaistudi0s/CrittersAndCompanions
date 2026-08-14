package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BabySpeedModifierBehaviour implements Behaviour {

    private static final UUID BABY_SPEED_ID = UUID.fromString("6a04e578-83eb-460d-a342-9cd59f3c5f49");
    
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
            if (speedAttribute.getModifier(BABY_SPEED_ID) == null) {
                speedAttribute.addPermanentModifier(new AttributeModifier(
                        BABY_SPEED_ID,
                        "baby_speed_modifier",
                        speedMultiplier,
                        AttributeModifier.Operation.MULTIPLY_BASE
                ));
            }
        } else {
            if (speedAttribute.getModifier(BABY_SPEED_ID) != null) {
                speedAttribute.removeModifier(BABY_SPEED_ID);
            }
        }
    }
}
