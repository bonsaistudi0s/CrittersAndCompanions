package com.github.eterdelta.crittersandcompanions.data.provider;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.platform.RegistryEntry;
import com.github.eterdelta.crittersandcompanions.registry.CACBlocks;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class CACLang extends LanguageProvider {

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
        add(CACEntities.GRAPPLING_HOOK.get(), "Grappling Hook");

        add(CACBlocks.SILK_COCOON.get(), "Silk Cocoon");

        add(CACItems.DUMBO_OCTOPUS_BUCKET.get(), "Bucket of Dumbo Octopus");
        add(CACItems.SEA_BUNNY_BUCKET.get(), "Bucket of Sea Bunny");
        add(CACItems.KOI_FISH_BUCKET.get(), "Bucket of Koi Fish");

        translate(CACItems.DIAMOND_DRAGONFLY_ARMOR.getKey());
        translate(CACItems.GOLD_DRAGONFLY_ARMOR.getKey());
        translate(CACItems.IRON_DRAGONFLY_ARMOR.getKey());

        translate(CACItems.KOI_FISH.getKey());
        translate(CACItems.CLAM.getKey());
        translate(CACItems.DRAGONFLY_WING.getKey());
        translate(CACItems.PEARL.getKey());
        translate(CACItems.SILK.getKey());
        translate(CACItems.SEA_BUNNY_SLIME_BLOCK.getKey());
        translate(CACItems.SILK_LEAD.getKey());
        translate(CACItems.GRAPPLING_HOOK.getKey());
        translate(CACItems.PEARL_NECKLACE_1.getKey());
        translate(CACItems.PEARL_NECKLACE_2.getKey());
        translate(CACItems.PEARL_NECKLACE_3.getKey());

        add("pearl_necklace.level", "Level %s");
        add("pearl_necklace.swim_speed", "+%s%% Base swim speed");
        add("pearl_necklace.drowned_range", "-%s%% Drowned detection range");
        add("pearl_necklace.guardian_range", "-%s%% Guardian detection range");

        subtitle(CACSounds.DRAGONFLY_AMBIENT.get(), "DragonFly Buzzes");
        subtitle(CACSounds.FERRET_AMBIENT.get(), "Ferret squeaks");
        subtitle(CACSounds.FERRET_DEATH.get(), "Ferret dies");
        subtitle(CACSounds.FERRET_HURT.get(), "Ferret hurts");
        subtitle(CACSounds.LEAF_INSECT_DEATH.get(), "Lead Insect dies");
        subtitle(CACSounds.LEAF_INSECT_HURT.get(), "Lead Insect hurts");
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
        subtitle(CACSounds.BITE_ATTACK.get(), "Animal bites");
        subtitle(CACSounds.BUBBLE_POP.get(), "Bubble pops");
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

    private void withSpawnEgg(RegistryEntry<? extends EntityType<?>> type) {
        var id = type.getKey().location();
        var translation = defaultTranslation(id.getPath());
        add(type.get(), translation);
        add("item.%s.%s_spawn_egg".formatted(id.getNamespace(), id.getPath()), translation + " Spawn Egg");
    }

}
