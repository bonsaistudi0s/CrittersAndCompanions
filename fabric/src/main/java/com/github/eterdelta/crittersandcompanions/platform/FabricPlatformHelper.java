package com.github.eterdelta.crittersandcompanions.platform;

import com.github.eterdelta.crittersandcompanions.compat.TrinketsCompat;
import com.github.eterdelta.crittersandcompanions.platform.service.IPlatformHelper;
import io.github.fabricators_of_create.porting_lib.attributes.PortingLibAttributes;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.material.Fluid;

public class FabricPlatformHelper implements IPlatformHelper {

    @SuppressWarnings("unchecked")
    @Override
    public <T> RegistryHelper<T> createRegistryHelper(ResourceKey<Registry<T>> registryKey, String modid) {
        if (registryKey.equals(Registries.ITEM)) {
            return (RegistryHelper<T>) new FabricRegistryHelper.ItemHelper(modid);
        }

        return new FabricRegistryHelper<>(registryKey, modid);
    }

    @Override
    public Attribute getSwimSpeedAttribute() {
        return PortingLibAttributes.SWIM_SPEED;
    }

    @Override
    public SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int primary, int secondary, Item.Properties properties) {
        return new SpawnEggItem(entityType.get(), primary, secondary, properties);
    }

    @Override
    public MobBucketItem createMobBucket(Supplier<? extends EntityType<? extends Mob>> entityType, Fluid fluid, SoundEvent emptySound, Item.Properties properties) {
        return new MobBucketItem(entityType.get(), fluid, emptySound, properties);
    }

    @Override
    public Stream<ItemStack> getAdditionalEquipment(Player player) {
        if (FabricLoader.getInstance().isModLoaded("trinkets")) return TrinketsCompat.getEquipment(player);
        return Stream.empty();
    }

}
