package io.github.bonsaistudi0s.crittersandcompanions.common.platform.forge;

import io.github.bonsaistudi0s.crittersandcompanions.forge.common.compat.CuriosCompat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.ModList;

import java.util.stream.Stream;

@SuppressWarnings("unused")
public class PlatformHooksImpl {

    public static boolean canAnimalBeTamed(Animal animal, Player player) {
        return !ForgeEventFactory.onAnimalTame(animal, player);
    }

    public static Stream<ItemStack> getAdditionalEquipment(Player player) {
        if (ModList.get().isLoaded("curios")) {
            return CuriosCompat.getEquipment(player);
        }

        return Stream.empty();
    }
}
