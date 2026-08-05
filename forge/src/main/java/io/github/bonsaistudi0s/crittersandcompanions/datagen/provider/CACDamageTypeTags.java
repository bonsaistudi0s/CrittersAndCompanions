package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CACDamageTypeTags extends DamageTypeTagsProvider {

    public CACDamageTypeTags(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, CrittersAndCompanions.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(CACTags.PANIC_ENVIRONMENTAL_CAUSES)
                .add(
                        DamageTypes.CACTUS,
                        DamageTypes.FREEZE,
                        DamageTypes.HOT_FLOOR,
                        DamageTypes.IN_FIRE,
                        DamageTypes.LAVA,
                        DamageTypes.LIGHTNING_BOLT,
                        DamageTypes.ON_FIRE
                );

        tag(CACTags.PANIC_CAUSES)
                .addTag(CACTags.PANIC_ENVIRONMENTAL_CAUSES)
                .add(
                        DamageTypes.ARROW,
                        DamageTypes.DRAGON_BREATH,
                        DamageTypes.EXPLOSION,
                        DamageTypes.FIREBALL,
                        DamageTypes.FIREWORKS,
                        DamageTypes.INDIRECT_MAGIC,
                        DamageTypes.MAGIC,
                        DamageTypes.MOB_ATTACK,
                        DamageTypes.MOB_PROJECTILE,
                        DamageTypes.PLAYER_ATTACK,
                        DamageTypes.PLAYER_EXPLOSION,
                        DamageTypes.SONIC_BOOM,
                        DamageTypes.STING,
                        DamageTypes.THROWN,
                        DamageTypes.TRIDENT,
                        DamageTypes.UNATTRIBUTED_FIREBALL,
                        DamageTypes.WITHER,
                        DamageTypes.WITHER_SKULL
                );
    }
}
