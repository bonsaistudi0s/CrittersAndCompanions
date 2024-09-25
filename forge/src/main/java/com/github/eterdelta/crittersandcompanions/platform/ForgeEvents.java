package com.github.eterdelta.crittersandcompanions.platform;

import com.github.eterdelta.crittersandcompanions.platform.service.IEvents;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ForgeEventFactory;

public class ForgeEvents implements IEvents {

    @Override
    public boolean canTame(Animal entity, Player player) {
        return !ForgeEventFactory.onAnimalTame(entity, player);
    }
}
