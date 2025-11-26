package com.github.eterdelta.crittersandcompanions.entity.brain.behaviour;

public interface BehaviourDriven {

    default Behaviours getBehaviours() {
        throw new IllegalStateException("BehaviourDriven not overwritten by mixin");
    }

    default <T extends Behaviour> T behaviour(Class<T> type) {
        return getBehaviours().the(type);
    }

    default void registerBehaviours(Behaviours behaviours) {
    }

}
