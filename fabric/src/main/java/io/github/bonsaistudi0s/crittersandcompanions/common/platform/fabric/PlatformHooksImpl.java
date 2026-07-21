package io.github.bonsaistudi0s.crittersandcompanions.common.platform.fabric;

import io.github.bonsaistudi0s.crittersandcompanions.fabric.common.compat.TrinketsCompat;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

@SuppressWarnings("unused")
public class PlatformHooksImpl {

    public static boolean canAnimalBeTamed(Animal animal, Player player) {
        return true;
    }

    public static Stream<ItemStack> getAdditionalEquipment(Player player) {
        if (FabricLoader.getInstance().isModLoaded("data.trinkets")) {
            return TrinketsCompat.getEquipment(player);
        }

        return Stream.empty();
    }
}
