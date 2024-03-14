package earth.terrarium.crittersandcompanions.common.registry.fabric;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class ModItemsImpl {
    public static Supplier<Item> createSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, Item.Properties properties) {
        return () -> new SpawnEggItem(type.get(), primaryColor, secondaryColor, properties);
    }

    public static Supplier<Item> createMobBucketItem(Supplier<? extends EntityType<? extends Mob>> type, Fluid fluid, SoundEvent sound, Item.Properties properties) {
        return () -> new MobBucketItem(type.get(), fluid, sound, properties);
    }
}
