package io.github.bonsaistudi0s.crittersandcompanions.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

public record AnimalTags(TagKey<Item> food, TagKey<Item> tempt) {

    public static AnimalTags create(ResourceKey<? extends EntityType<?>> key) {
        var id = key.location();
        var tempt = TagKey.create(Registries.ITEM, id.withSuffix("_tempt_items"));
        var food = TagKey.create(Registries.ITEM, id.withSuffix("_food"));
        return new AnimalTags(food, tempt);
    }

    public Goal temptGoal(PathfinderMob mob) {
        return new TemptGoal(mob, 1.0D, Ingredient.of(tempt()), false);
    }

}
