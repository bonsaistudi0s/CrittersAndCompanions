package com.github.eterdelta.crittersandcompanions.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class AcornItem extends Item {

    public static final FoodProperties FOOD = new FoodProperties.Builder()
            .nutrition(1)
            .saturationModifier(0.1f)
            .build();

    public AcornItem(Properties properties) {
        super(properties.food(FOOD));
    }
}
