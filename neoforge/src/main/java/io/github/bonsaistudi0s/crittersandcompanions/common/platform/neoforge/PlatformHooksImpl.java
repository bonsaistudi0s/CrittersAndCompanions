package io.github.bonsaistudi0s.crittersandcompanions.common.platform.neoforge;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.EventHooks;

import java.util.stream.Stream;

import io.github.bonsaistudi0s.crittersandcompanions.neoforge.common.compat.CuriosCompat;

@SuppressWarnings("unused")
public class PlatformHooksImpl {

    public static boolean canAnimalBeTamed(Animal animal, Player player) {
        return !EventHooks.onAnimalTame(animal, player);
    }

    public static Stream<ItemStack> getAdditionalEquipment(Player player) {
        if (ModList.get().isLoaded("curios")) {
            return CuriosCompat.getEquipment(player);
        }

        return Stream.empty();
    }
}
