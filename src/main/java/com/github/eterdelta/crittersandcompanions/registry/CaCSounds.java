package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CaCSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CrittersAndCompanions.MODID);

    public static final RegistryObject<SoundEvent> DRAGONFLY_AMBIENT = SOUNDS.register("entity.dragonfly.ambient", () -> new SoundEvent(new ResourceLocation(CrittersAndCompanions.MODID, "entity.dragonfly.ambient")));
}
