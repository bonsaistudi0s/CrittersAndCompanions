package io.github.bonsaistudi0s.crittersandcompanions.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import dev.architectury.registry.registries.RegistrySupplier;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACBlocks;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACSounds;

public final class CACLang extends LanguageProvider {

    public CACLang(PackOutput output) {
        super(output, CrittersAndCompanions.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + CrittersAndCompanions.MODID, "Critters and Companions");

        withSpawnEgg(CACEntities.DRAGONFLY);
        withSpawnEgg(CACEntities.OTTER);
        withSpawnEgg(CACEntities.KOI_FISH);
        withSpawnEgg(CACEntities.JUMPING_SPIDER);
        withSpawnEgg(CACEntities.SEA_BUNNY);
        withSpawnEgg(CACEntities.SHIMA_ENAGA);
        withSpawnEgg(CACEntities.FERRET);
        withSpawnEgg(CACEntities.DUMBO_OCTOPUS);
        withSpawnEgg(CACEntities.RED_PANDA);
        withSpawnEgg(CACEntities.LEAF_INSECT);
        withSpawnEgg(CACEntities.LADYBUG);
        withSpawnEgg(CACEntities.STAG_BEETLE);
        withSpawnEggAndCustomTranslation(CACEntities.ROLY_POLY, "Roly-Poly");
        withSpawnEgg(CACEntities.SNAIL);
        withSpawnEgg(CACEntities.STICK_BUG);
        withSpawnEggAndCustomTranslation(CACEntities.WEEVIL, "Acorn Weevil");
        add(CACEntities.GRAPPLING_HOOK.get(), "Grappling Hook");

        translate(CACBlocks.SILK_COCOON.getKey());
        translate(CACBlocks.SEA_BUNNY_SLIME_BLOCK.getKey());

        add(CACItems.DUMBO_OCTOPUS_BUCKET.get(), "Bucket of Dumbo Octopus");
        add(CACItems.SEA_BUNNY_BUCKET.get(), "Bucket of Sea Bunny");
        add(CACItems.KOI_FISH_BUCKET.get(), "Bucket of Koi Fish");

        translate(CACItems.NETHERITE_DRAGONFLY_ARMOR.getKey());
        translate(CACItems.DIAMOND_DRAGONFLY_ARMOR.getKey());
        translate(CACItems.GOLD_DRAGONFLY_ARMOR.getKey());
        translate(CACItems.IRON_DRAGONFLY_ARMOR.getKey());

        translate(CACItems.KOI_FISH.getKey());
        translate(CACItems.CLAM.getKey());
        translate(CACItems.DRAGONFLY_WING.getKey());
        translate(CACItems.PEARL.getKey());
        translate(CACItems.SILK.getKey());
        translate(CACItems.SEA_BUNNY_SLIME_BOTTLE.getKey());
        translate(CACItems.SILK_LEAD.getKey());
        translate(CACItems.GRAPPLING_HOOK.getKey());
        translate(CACItems.PEARL_NECKLACE_1.getKey());
        translate(CACItems.PEARL_NECKLACE_2.getKey());
        translate(CACItems.PEARL_NECKLACE_3.getKey());

        translate(CACItems.ACORN_HAT.getKey());
        translate(CACItems.ACORN.getKey());
        translate(CACItems.SNAIL_SLIME_BOTTLE.getKey());

        add("item.minecraft.potion.effect.resistance", "Potion of Resistance");
        add("item.minecraft.splash_potion.effect.resistance", "Splash Potion of Resistance");
        add("item.minecraft.lingering_potion.effect.resistance", "Lingering Potion of Resistance");

        add("pearl_necklace.level", "Level %s");
        add("pearl_necklace.swim_speed", "+%s%% Base swim speed");
        add("pearl_necklace.drowned_range", "-%s%% Drowned detection range");
        add("pearl_necklace.guardian_range", "-%s%% Guardian detection range");

        subtitle(CACSounds.DRAGONFLY_AMBIENT_LOOP.get(), "Dragonfly buzzes");
        subtitle(CACSounds.FERRET_AMBIENT.get(), "Ferret squeaks");
        subtitle(CACSounds.FERRET_DEATH.get(), "Ferret dies");
        subtitle(CACSounds.FERRET_HURT.get(), "Ferret hurts");
        subtitle(CACSounds.LEAF_INSECT_DEATH.get(), "Leaf insect dies");
        subtitle(CACSounds.LEAF_INSECT_HURT.get(), "Leaf insect hurts");
        subtitle(CACSounds.LEAF_INSECT_EAT.get(), "Leaf insect eats");
        subtitle(CACSounds.OTTER_AMBIENT.get(), "Otter squeaks");
        subtitle(CACSounds.OTTER_DEATH.get(), "Otter dies");
        subtitle(CACSounds.OTTER_HURT.get(), "Otter hurts");
        subtitle(CACSounds.OTTER_SWIM.get(), "Otter swims");
        subtitle(CACSounds.OTTER_EAT.get(), "Otter eats");
        subtitle(CACSounds.RED_PANDA_AMBIENT.get(), "Red Panda squeaks");
        subtitle(CACSounds.RED_PANDA_DEATH.get(), "Red Panda dies");
        subtitle(CACSounds.RED_PANDA_HURT.get(), "Red Panda hurts");
        subtitle(CACSounds.SEA_BUNNY_DEATH.get(), "Sea Bunny dies");
        subtitle(CACSounds.SEA_BUNNY_HURT.get(), "Sea Bunny hurts");
        subtitle(CACSounds.SHIMA_ENAGA_AMBIENT.get(), "Shima Enaga sings");
        subtitle(CACSounds.SHIMA_ENAGA_FLY.get(), "Shima Enaga flutters");
        subtitle(CACSounds.BITE_ATTACK.get(), "Animal bites");
        subtitle(CACSounds.BUBBLE_POP.get(), "Bubble pops");
        subtitle(CACSounds.BUGS_HURT.get(), "Bug chitters");
        subtitle(CACSounds.BUGS_WALK_LOOP.get(), "Bug patters");
        subtitle(CACSounds.SNAIL_WALK_LOOP.get(), "Snail squelches");
        subtitle(CACSounds.SNAIL_GARY_IDLE.get(), "Gary meows");
        subtitle(CACSounds.SNAIL_GARY_PURR.get(), "Gary purrs");
        subtitle(CACSounds.SNAIL_GARY_SING.get(), "Gary sings");
        subtitle(CACSounds.SNAIL_GARY_HURT.get(), "Gary screams");
        subtitle(CACSounds.LADYBUG_FLY_LOOP.get(), "Ladybug flutters");
    }

    private void subtitle(SoundEvent sound, String translation) {
        add("subtitles.%s.%s".formatted(sound.getLocation().getNamespace(), sound.getLocation().getPath()), translation);
    }

    private String defaultTranslation(String key) {
        return Arrays.stream(key.split("_"))
                .map(it -> it.substring(0, 1).toUpperCase(Locale.ROOT) + it.substring(1))
                .collect(Collectors.joining(" "));
    }

    private void translate(ResourceKey<?> subject) {
        var translation = defaultTranslation(subject.location().getPath());
        var key = "%s.%s.%s".formatted(subject.registry().getPath(), subject.location().getNamespace(), subject.location().getPath());
        add(key, translation);
    }

    private void withSpawnEgg(RegistrySupplier<? extends EntityType<?>> type) {
        var id = type.getKey().location();
        var translation = defaultTranslation(id.getPath());
        add(type.get(), translation);
        add("item.%s.%s_spawn_egg".formatted(id.getNamespace(), id.getPath()), translation + " Spawn Egg");
    }

    private void withSpawnEggAndCustomTranslation(RegistrySupplier<? extends EntityType<?>> type, String customTranslation) {
        add(type.get(), customTranslation);
        addSpawnEgg(type, customTranslation);
    }

    private void addSpawnEgg(RegistrySupplier<? extends EntityType<?>> type, String translation) {
        var id = type.getKey().location();
        add("item.%s.%s_spawn_egg".formatted(id.getNamespace(), id.getPath()), translation + " Spawn Egg");
    }
}
