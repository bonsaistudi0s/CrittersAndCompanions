package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour;

import net.minecraft.world.entity.TamableAnimal;

public record HealthRegenerationBehaviour(TamableAnimal owner) implements Behaviour {

    private static final int TICK_INTERVAL = 20 * 60;

    @Override
    public void serverTick() {
        if (!owner.isTame() || owner.isDeadOrDying()) {
            return;
        }

        if (owner.tickCount % TICK_INTERVAL != 0) {
            return;
        }
        if (owner.getHealth() >= owner.getMaxHealth()) {
            return;
        }

        owner.heal(1.0F);
    }
}
