package io.github.bonsaistudi0s.crittersandcompanions.common.mixin.behaviour;

import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.BehaviourDriven;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviours;

@Mixin(Entity.class)
public class EntityMixin implements BehaviourDriven {

    @Unique
    private Behaviours cac$createBehaviours() {
        var behaviours = new Behaviours();
        registerBehaviours(behaviours);
        return behaviours;
    }

    @Unique
    private final Behaviours cac$behaviours = cac$createBehaviours();

    @Override
    public Behaviours getBehaviours() {
        return cac$behaviours;
    }

}
