package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;

public class CACSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(CrittersAndCompanions.MODID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> BITE_ATTACK = SOUNDS.register("entity.bite_attack", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.bite_attack")));
    public static final RegistrySupplier<SoundEvent> BUBBLE_POP = SOUNDS.register("entity.bubble_pop", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.bubble_pop")));
    public static final RegistrySupplier<SoundEvent> DRAGONFLY_AMBIENT_LOOP = SOUNDS.register("entity.dragonfly.ambient", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.dragonfly.ambient")));
    public static final RegistrySupplier<SoundEvent> FERRET_AMBIENT = SOUNDS.register("entity.ferret.ambient", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.ferret.ambient")));
    public static final RegistrySupplier<SoundEvent> FERRET_DEATH = SOUNDS.register("entity.ferret.death", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.ferret.death")));
    public static final RegistrySupplier<SoundEvent> FERRET_HURT = SOUNDS.register("entity.ferret.hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.ferret.hurt")));
    public static final RegistrySupplier<SoundEvent> LEAF_INSECT_DEATH = SOUNDS.register("entity.leaf_insect.death", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.leaf_insect.death")));
    public static final RegistrySupplier<SoundEvent> LEAF_INSECT_HURT = SOUNDS.register("entity.leaf_insect.hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.leaf_insect.hurt")));
    public static final RegistrySupplier<SoundEvent> OTTER_AMBIENT = SOUNDS.register("entity.otter.ambient", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.ambient")));
    public static final RegistrySupplier<SoundEvent> OTTER_DEATH = SOUNDS.register("entity.otter.death", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.death")));
    public static final RegistrySupplier<SoundEvent> OTTER_EAT = SOUNDS.register("entity.otter.eat", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.eat")));
    public static final RegistrySupplier<SoundEvent> OTTER_CLAM_BREAK = SOUNDS.register("entity.otter.clam_break", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.clam_break")));
    public static final RegistrySupplier<SoundEvent> OTTER_CLAM_BREAK_LAND = SOUNDS.register("entity.otter.clam_break_land", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.clam_break_land")));
    public static final RegistrySupplier<SoundEvent> OTTER_HURT = SOUNDS.register("entity.otter.hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.hurt")));
    public static final RegistrySupplier<SoundEvent> OTTER_SWIM = SOUNDS.register("entity.otter.swim", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.swim")));
    public static final RegistrySupplier<SoundEvent> RED_PANDA_AMBIENT = SOUNDS.register("entity.red_panda.ambient", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.red_panda.ambient")));
    public static final RegistrySupplier<SoundEvent> RED_PANDA_DEATH = SOUNDS.register("entity.red_panda.death", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.red_panda.death")));
    public static final RegistrySupplier<SoundEvent> RED_PANDA_HURT = SOUNDS.register("entity.red_panda.hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.red_panda.hurt")));
    public static final RegistrySupplier<SoundEvent> SEA_BUNNY_DEATH = SOUNDS.register("entity.sea_bunny.death", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.sea_bunny.death")));
    public static final RegistrySupplier<SoundEvent> SEA_BUNNY_HURT = SOUNDS.register("entity.sea_bunny.hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.sea_bunny.hurt")));
    public static final RegistrySupplier<SoundEvent> SHIMA_ENAGA_AMBIENT = SOUNDS.register("entity.shima_enaga.ambient", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.shima_enaga.ambient")));

    public static final RegistrySupplier<SoundEvent> BUGS_WALK_LOOP = SOUNDS.register("entity.bugs.walk", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.bugs.walk")));
    public static final RegistrySupplier<SoundEvent> BUGS_HURT = SOUNDS.register("entity.bugs.hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.bugs.hurt")));
    public static final RegistrySupplier<SoundEvent> SNAIL_WALK_LOOP = SOUNDS.register("entity.snail.walk", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.snail.walk")));
    public static final RegistrySupplier<SoundEvent> SNAIL_GARY_IDLE = SOUNDS.register("entity.snail.gary_idle", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.snail.gary_idle")));
    public static final RegistrySupplier<SoundEvent> SNAIL_GARY_PURR = SOUNDS.register("entity.snail.gary_purr", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.snail.gary_purr")));
    public static final RegistrySupplier<SoundEvent> SNAIL_GARY_SING = SOUNDS.register("entity.snail.gary_sing", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.snail.gary_sing")));
    public static final RegistrySupplier<SoundEvent> SNAIL_GARY_HURT = SOUNDS.register("entity.snail.gary_hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.snail.gary_hurt")));
    public static final RegistrySupplier<SoundEvent> LADYBUG_FLY_LOOP = SOUNDS.register("entity.ladybug.fly", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.ladybug.fly")));

    public static void init() {
        SOUNDS.register();
    }
}
