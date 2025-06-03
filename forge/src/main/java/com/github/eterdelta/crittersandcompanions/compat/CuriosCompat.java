package com.github.eterdelta.crittersandcompanions.compat;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class CuriosCompat {

    private static Stream<ItemStack> resolve(ICurioStacksHandler handler) {
        var stacks = handler.getStacks();
        return IntStream.range(0, handler.getSlots()).mapToObj(stacks::getStackInSlot);
    }

    public static Stream<ItemStack> getEquipment(Player player) {
        return CuriosApi.getCuriosHelper().getCuriosHandler(player)
                .map(ICuriosItemHandler::getCurios)
                .stream()
                .flatMap(it -> it.values().stream())
                .flatMap(CuriosCompat::resolve);
    }

}
