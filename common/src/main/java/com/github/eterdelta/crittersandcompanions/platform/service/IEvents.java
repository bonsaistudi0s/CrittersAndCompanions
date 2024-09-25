package com.github.eterdelta.crittersandcompanions.platform.service;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

public interface IEvents {

    boolean canTame(Animal entity, Player player);

}
