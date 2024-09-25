package com.github.eterdelta.crittersandcompanions.platform.service;

import com.github.eterdelta.crittersandcompanions.platform.RegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public interface IPlatformHelper {

    <T> RegistryHelper<T> createRegistryHelper(ResourceKey<Registry<T>> registryKey, String modid);

    Attribute getSwimSpeedAttribute();

    SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int primary, int secondary, Item.Properties properties);

    MobBucketItem createMobBucket(Supplier<? extends EntityType<? extends Mob>> entityType, Fluid fluid, SoundEvent emptySound, Item.Properties properties);
}
