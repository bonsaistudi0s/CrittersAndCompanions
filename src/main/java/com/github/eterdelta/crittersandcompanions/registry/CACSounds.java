package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CACSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CrittersAndCompanions.MODID);

    public static final RegistryObject<SoundEvent> BITE_ATTACK = SOUNDS.register("entity.bite_attack", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.bite_attack")));
    public static final RegistryObject<SoundEvent> BUBBLE_POP = SOUNDS.register("entity.bubble_pop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.bubble_pop")));
    public static final RegistryObject<SoundEvent> DRAGONFLY_AMBIENT = SOUNDS.register("entity.dragonfly.ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.dragonfly.ambient")));
    public static final RegistryObject<SoundEvent> FERRET_AMBIENT = SOUNDS.register("entity.ferret.ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.ferret.ambient")));
    public static final RegistryObject<SoundEvent> FERRET_DEATH = SOUNDS.register("entity.ferret.death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.ferret.death")));
    public static final RegistryObject<SoundEvent> FERRET_HURT = SOUNDS.register("entity.ferret.hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.ferret.hurt")));
    public static final RegistryObject<SoundEvent> LEAF_INSECT_DEATH = SOUNDS.register("entity.leaf_insect.death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.leaf_insect.death")));
    public static final RegistryObject<SoundEvent> LEAF_INSECT_HURT = SOUNDS.register("entity.leaf_insect.hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.leaf_insect.hurt")));
    public static final RegistryObject<SoundEvent> OTTER_AMBIENT = SOUNDS.register("entity.otter.ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.otter.ambient")));
    public static final RegistryObject<SoundEvent> OTTER_DEATH = SOUNDS.register("entity.otter.death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.otter.death")));
    public static final RegistryObject<SoundEvent> OTTER_EAT = SOUNDS.register("entity.otter.eat", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.otter.eat")));
    public static final RegistryObject<SoundEvent> OTTER_HURT = SOUNDS.register("entity.otter.hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.otter.hurt")));
    public static final RegistryObject<SoundEvent> OTTER_SWIM = SOUNDS.register("entity.otter.swim", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.otter.swim")));
    public static final RegistryObject<SoundEvent> RED_PANDA_AMBIENT = SOUNDS.register("entity.red_panda.ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.red_panda.ambient")));
    public static final RegistryObject<SoundEvent> RED_PANDA_DEATH = SOUNDS.register("entity.red_panda.death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.red_panda.death")));
    public static final RegistryObject<SoundEvent> RED_PANDA_HURT = SOUNDS.register("entity.red_panda.hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.red_panda.hurt")));
    public static final RegistryObject<SoundEvent> SEA_BUNNY_DEATH = SOUNDS.register("entity.sea_bunny.death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.sea_bunny.death")));
    public static final RegistryObject<SoundEvent> SEA_BUNNY_HURT = SOUNDS.register("entity.sea_bunny.hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.sea_bunny.hurt")));
    public static final RegistryObject<SoundEvent> SHIMA_ENAGA_AMBIENT = SOUNDS.register("entity.shima_enaga.ambient", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.shima_enaga.ambient")));
}
