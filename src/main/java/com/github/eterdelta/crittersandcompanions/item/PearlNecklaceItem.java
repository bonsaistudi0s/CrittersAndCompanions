package com.github.eterdelta.crittersandcompanions.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class PearlNecklaceItem extends Item {
    private final int level;

    public PearlNecklaceItem(Properties properties, int necklaceLevel) {
        super(properties);
        this.level = necklaceLevel;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> components, TooltipFlag tooltipFlag) {
        components.add(new TranslatableComponent("pearl_necklace.level", this.level).withStyle(ChatFormatting.DARK_GRAY));
        components.add(TextComponent.EMPTY);

        components.add(new TranslatableComponent("pearl_necklace.swim_speed", this.level * 10).withStyle(ChatFormatting.GRAY));
        components.add(new TranslatableComponent("pearl_necklace.drowned_range", this.level * 20).withStyle(ChatFormatting.GRAY));
        if (this.level > 1) {
            components.add(new TranslatableComponent("pearl_necklace.guardian_range", this.level * 20).withStyle(ChatFormatting.GRAY));
        }
    }

    public int getLevel() {
        return this.level;
    }
}
