package io.github.bonsaistudi0s.crittersandcompanions.common.platform;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformHooks {

    @ExpectPlatform
    public static boolean canAnimalBeTamed(Animal animal, Player player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Stream<ItemStack> getAdditionalEquipment(Player player) {
        throw new AssertionError();
    }
}
