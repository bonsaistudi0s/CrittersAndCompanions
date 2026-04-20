package com.github.eterdelta.crittersandcompanions.platform;

import com.github.eterdelta.crittersandcompanions.compat.CuriosCompat;
import com.github.eterdelta.crittersandcompanions.platform.service.IPlatformHelper;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.NeoForgeMod;

public class ForgePlatformHelper implements IPlatformHelper {

    @SuppressWarnings("unchecked")
    @Override
    public <T> RegistryHelper<T> createRegistryHelper(ResourceKey<Registry<T>> registryKey, String modid) {
        if (registryKey.equals(Registries.ITEM)) {
            return (RegistryHelper<T>) new ForgeRegistryHelper.ItemHelper(modid);
        }

        return new ForgeRegistryHelper<>(registryKey, modid);
    }

    @Override
    public Holder<Attribute> getSwimSpeedAttribute() {
        return NeoForgeMod.SWIM_SPEED;
    }

    @Override
    public SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int primary, int secondary, Item.Properties properties) {
        return new DeferredSpawnEggItem(entityType, primary, secondary, properties);
    }

    @Override
    public Stream<ItemStack> getAdditionalEquipment(Player player) {
        if(ModList.get().isLoaded("curios")) return CuriosCompat.getEquipment(player);
        return Stream.empty();
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
