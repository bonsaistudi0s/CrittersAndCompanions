package com.github.eterdelta.crittersandcompanions.platform.service;

import com.github.eterdelta.crittersandcompanions.platform.RegistryHelper;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

public interface IPlatformHelper {

    <T> RegistryHelper<T> createRegistryHelper(ResourceKey<Registry<T>> registryKey, String modid);

    Holder<Attribute> getSwimSpeedAttribute();

    SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int primary, int secondary, Item.Properties properties);

    Stream<ItemStack> getAdditionalEquipment(Player player);

    Path getConfigDir();
}
