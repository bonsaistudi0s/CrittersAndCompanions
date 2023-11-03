package com.github.eterdelta.crittersandcompanions.handler;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.capability.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID)

public class CapabilityHandler {

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            LazyOptional<IBubbleStateCapability> bubbleState = LazyOptional.of(BubbleState::new);
            ICapabilityProvider bubbleStateProvider = new ICapabilityProvider() {
                @Override
                public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
                    if (capability == CACCapabilities.BUBBLE_STATE) {
                        return bubbleState.cast();
                    }
                    return LazyOptional.empty();
                }
            };
            event.addCapability(new ResourceLocation(CrittersAndCompanions.MODID, "bubble_state"), bubbleStateProvider);

            LazyOptional<IGrapplingStateCapability> grapplingState = LazyOptional.of(GrapplingState::new);
            ICapabilityProvider grapplingStateProvider = new ICapabilityProvider() {
                @Override
                public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
                    if (capability == CACCapabilities.GRAPPLING_STATE) {
                        return grapplingState.cast();
                    }
                    return LazyOptional.empty();
                }
            };
            event.addCapability(new ResourceLocation(CrittersAndCompanions.MODID, "grappling_state"), grapplingStateProvider);
        }

        if (event.getObject() instanceof LivingEntity) {
            LazyOptional<ISilkLeashStateCapability> optionalSilkLeashState = LazyOptional.of(SilkLeashState::new);
            ICapabilityProvider silkLeashStateProvider = new ICapabilityProvider() {
                @Override
                public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction direction) {
                    if (capability == CACCapabilities.SILK_LEASH_STATE) {
                        return optionalSilkLeashState.cast();
                    }
                    return LazyOptional.empty();
                }
            };
            event.addCapability(new ResourceLocation(CrittersAndCompanions.MODID, "silk_leash_state"), silkLeashStateProvider);
        }
    }
}
