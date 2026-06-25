package com.github.eterdelta.crittersandcompanions.entity.brain.behaviour;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.TamableAnimal;

public record LadybugHealingAuraBehaviour(TamableAnimal owner) implements Behaviour {

    private static final int RADIUS = 4;
    private static final int TICK_INTERVAL = 20 * 9;

    @Override
    public void serverTick() {
        if (!owner.isTame()) return;
        if (owner.tickCount % TICK_INTERVAL != 0) return;

        owner.level()
                .getEntitiesOfClass(
                        TamableAnimal.class,
                        owner.getBoundingBox().inflate(RADIUS),
                        tamableAnimal -> {
                            var isSelf = tamableAnimal == owner;
                            if (isSelf) {
                                return false;
                            }

                            if (!tamableAnimal.isTame()) {
                                return false;
                            }

                            var isAtFullHealth = tamableAnimal.getHealth() >= tamableAnimal.getMaxHealth();
                            if (isAtFullHealth) {
                                return false;
                            }

                            return true;
                        }
                )
                .forEach(target -> target.addEffect(
                        new MobEffectInstance(MobEffects.REGENERATION, TICK_INTERVAL + 20, 0, false, true))
                );
    }
}
