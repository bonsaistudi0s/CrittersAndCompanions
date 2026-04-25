package com.github.eterdelta.crittersandcompanions.entity.brain.behaviour;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class Behaviours {

    private final Map<Class<? extends Behaviour>, Behaviour> entries = new LinkedHashMap<>();

    public Behaviours add(Behaviour behaviour) {
        if (this.entries.put(behaviour.getClass(), behaviour) != null) {
            throw new IllegalArgumentException("cannot have two behaviours of the same class");
        }
        return this;
    }

    public Iterable<Behaviour> all() {
        return entries.values();
    }

    public void forEach(Consumer<Behaviour> consumer) {
        all().forEach(consumer);
    }

    @SuppressWarnings("unchecked")
    public <T extends Behaviour> Optional<T> optional(Class<T> type) {
        return Optional.ofNullable(entries.get(type)).map(it -> (T) it);
    }

    public <T extends Behaviour> T the(Class<T> type) {
        return optional(type).orElseThrow();
    }
}
