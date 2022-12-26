package com.github.eterdelta.crittersandcompanions.handler;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.KoiFishEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID)
public class PlayerHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.side.isServer()) {
                List<KoiFishEntity> nearKoiFishes = event.player.getLevel().getEntitiesOfClass(KoiFishEntity.class, event.player.getBoundingBox().inflate(10.0D), EntitySelector.ENTITY_STILL_ALIVE);

                if (nearKoiFishes.size() >= 3) {
                    event.player.addEffect(new MobEffectInstance(MobEffects.LUCK));
                }
            }
        }
    }
}
