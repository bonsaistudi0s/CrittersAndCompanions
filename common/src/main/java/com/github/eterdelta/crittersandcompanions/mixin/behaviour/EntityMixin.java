package com.github.eterdelta.crittersandcompanions.mixin.behaviour;

import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.BehaviourDriven;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.Behaviours;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
