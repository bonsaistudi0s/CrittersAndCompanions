package io.github.bonsaistudi0s.crittersandcompanions.common.item;

import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.platform.PlatformHooks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class PearlNecklaceItem extends Item {

    private static Stream<ItemStack> getEquipment(Player player) {
        return Stream.of(
                player.getInventory().items.stream(),
                PlatformHooks.getAdditionalEquipment(player)
        ).flatMap(Function.identity());
    }

    public static Optional<PearlNecklaceItem> getWearing(Entity entity) {
        if (!(entity instanceof Player player)) return Optional.empty();
        return getEquipment(player)
                .map(ItemStack::getItem)
                .filter(it -> it instanceof PearlNecklaceItem)
                .map(it -> (PearlNecklaceItem) it)
                .max(Comparator.comparing(PearlNecklaceItem::getLevel));
    }

    private final int level;

    public PearlNecklaceItem(Properties properties, int necklaceLevel) {
        super(properties);
        this.level = necklaceLevel;
    }

    private String percentage(int level, double base) {
        return String.format("%.0f", level * 100 * base);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(Component.translatable("pearl_necklace.level", this.level).withStyle(ChatFormatting.DARK_GRAY));
        components.add(Component.empty());

        components.add(Component.translatable("pearl_necklace.swim_speed", percentage(this.level, CACCommonConfig.HANDLER.instance().necklace.swimSpeed)).withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("pearl_necklace.drowned_range", percentage(this.level, CACCommonConfig.HANDLER.instance().necklace.drownedRangeDebuff)).withStyle(ChatFormatting.GRAY));
        if (this.level > 1) {
            components.add(Component.translatable("pearl_necklace.guardian_range", percentage(this.level, CACCommonConfig.HANDLER.instance().necklace.guardianRangeDebuff)).withStyle(ChatFormatting.GRAY));
        }
    }

    public int getLevel() {
        return this.level;
    }

}
