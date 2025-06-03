package com.github.eterdelta.crittersandcompanions.platform;

import com.github.eterdelta.crittersandcompanions.compat.CuriosCompat;
import com.github.eterdelta.crittersandcompanions.platform.service.IPlatformHelper;
import java.util.function.Supplier;
import java.util.stream.Stream;
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
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.ModList;

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
    public Attribute getSwimSpeedAttribute() {
        return ForgeMod.SWIM_SPEED.get();
    }

    @Override
    public SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int primary, int secondary, Item.Properties properties) {
        return new ForgeSpawnEggItem(entityType, primary, secondary, properties);
    }

    @Override
    public MobBucketItem createMobBucket(Supplier<? extends EntityType<? extends Mob>> entityType, Fluid fluid, SoundEvent emptySound, Item.Properties properties) {
        return new MobBucketItem(entityType, () -> fluid, () -> emptySound, properties);
    }

    @Override
    public Stream<ItemStack> getAdditionalEquipment(Player player) {
        if(ModList.get().isLoaded("curios")) return CuriosCompat.getEquipment(player);
        return Stream.empty();
    }

}
