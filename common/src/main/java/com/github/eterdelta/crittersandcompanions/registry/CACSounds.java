package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import com.github.eterdelta.crittersandcompanions.platform.RegistryHelper;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class CACSounds {
    private static final RegistryHelper<SoundEvent> SOUNDS = Services.PLATFORM.createRegistryHelper(Registries.SOUND_EVENT, CrittersAndCompanions.MODID);

    public static final RegistryEntry<SoundEvent> BITE_ATTACK = SOUNDS.register("entity.bite_attack", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.bite_attack")));
    public static final RegistryEntry<SoundEvent> BUBBLE_POP = SOUNDS.register("entity.bubble_pop", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.bubble_pop")));
    public static final RegistryEntry<SoundEvent> DRAGONFLY_AMBIENT = SOUNDS.register("entity.dragonfly.ambient", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.dragonfly.ambient")));
    public static final RegistryEntry<SoundEvent> FERRET_AMBIENT = SOUNDS.register("entity.ferret.ambient", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.ferret.ambient")));
    public static final RegistryEntry<SoundEvent> FERRET_DEATH = SOUNDS.register("entity.ferret.death", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.ferret.death")));
    public static final RegistryEntry<SoundEvent> FERRET_HURT = SOUNDS.register("entity.ferret.hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.ferret.hurt")));
    public static final RegistryEntry<SoundEvent> LEAF_INSECT_DEATH = SOUNDS.register("entity.leaf_insect.death", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.leaf_insect.death")));
    public static final RegistryEntry<SoundEvent> LEAF_INSECT_HURT = SOUNDS.register("entity.leaf_insect.hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.leaf_insect.hurt")));
    public static final RegistryEntry<SoundEvent> OTTER_AMBIENT = SOUNDS.register("entity.otter.ambient", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.ambient")));
    public static final RegistryEntry<SoundEvent> OTTER_DEATH = SOUNDS.register("entity.otter.death", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.death")));
    public static final RegistryEntry<SoundEvent> OTTER_EAT = SOUNDS.register("entity.otter.eat", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.eat")));
    public static final RegistryEntry<SoundEvent> OTTER_CLAM_BREAK = SOUNDS.register("entity.otter.clam_break", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.clam_break")));
    public static final RegistryEntry<SoundEvent> OTTER_CLAM_BREAK_LAND = SOUNDS.register("entity.otter.clam_break_land", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.clam_break_land")));
    public static final RegistryEntry<SoundEvent> OTTER_HURT = SOUNDS.register("entity.otter.hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.hurt")));
    public static final RegistryEntry<SoundEvent> OTTER_SWIM = SOUNDS.register("entity.otter.swim", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.otter.swim")));
    public static final RegistryEntry<SoundEvent> RED_PANDA_AMBIENT = SOUNDS.register("entity.red_panda.ambient", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.red_panda.ambient")));
    public static final RegistryEntry<SoundEvent> RED_PANDA_DEATH = SOUNDS.register("entity.red_panda.death", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.red_panda.death")));
    public static final RegistryEntry<SoundEvent> RED_PANDA_HURT = SOUNDS.register("entity.red_panda.hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.red_panda.hurt")));
    public static final RegistryEntry<SoundEvent> SEA_BUNNY_DEATH = SOUNDS.register("entity.sea_bunny.death", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.sea_bunny.death")));
    public static final RegistryEntry<SoundEvent> SEA_BUNNY_HURT = SOUNDS.register("entity.sea_bunny.hurt", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.sea_bunny.hurt")));
    public static final RegistryEntry<SoundEvent> SHIMA_ENAGA_AMBIENT = SOUNDS.register("entity.shima_enaga.ambient", () -> SoundEvent.createVariableRangeEvent(CrittersAndCompanions.createId("entity.shima_enaga.ambient")));

    public static void init() {
        // Load the class
    }

}
