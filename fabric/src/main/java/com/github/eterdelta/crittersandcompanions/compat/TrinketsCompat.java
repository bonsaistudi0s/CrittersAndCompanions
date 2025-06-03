package com.github.eterdelta.crittersandcompanions.compat;

import dev.emi.trinkets.api.TrinketsApi;
import java.util.stream.Stream;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TrinketsCompat {

    public static Stream<ItemStack> getEquipment(Player player) {
        return TrinketsApi.getTrinketComponent(player)
                .stream()
                .flatMap(it -> it.getAllEquipped().stream())
                .map(Tuple::getB);
    }

}
