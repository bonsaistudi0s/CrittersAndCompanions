package com.github.eterdelta.crittersandcompanions.config;

import com.github.eterdelta.crittersandcompanions.CACWorldGen;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.ModConfigSpec;

public class FabricCommonConfig extends CACCommonConfig {

    private final Map<ResourceKey<? extends EntityType<?>>, Supplier<CACWorldGen.SpawnValues>> spawnValues = new HashMap<>();

    private void registerSpawns(ModConfigSpec.Builder builder, ResourceKey<? extends EntityType<?>> key, CACWorldGen.SpawnValues defaultValues) {
        builder.push(key.location().getPath());

        var weight = builder.defineInRange("weight", defaultValues.weight(), 0, 128);
        var min = builder.defineInRange("min", defaultValues.min(), 1, 128);
        var max = builder.defineInRange("max", defaultValues.max(), 1, 128);

        spawnValues.put(key, () -> new CACWorldGen.SpawnValues(weight.getAsInt(), min.get(), max.getAsInt()));

        builder.pop();
    }

    public CACWorldGen.SpawnValues getSpawnValues(ResourceKey<? extends EntityType<?>> key) {
        return Objects.requireNonNull(spawnValues.get(key)).get();
    }

    public FabricCommonConfig(ModConfigSpec.Builder builder) {
        super(builder);

        builder.push("spawn_rates");

        registerSpawns(builder, CACEntities.LEAF_INSECT.getKey(), new CACWorldGen.SpawnValues(14, 1, 1));
        registerSpawns(builder, CACEntities.RED_PANDA.getKey(), new CACWorldGen.SpawnValues(8, 1, 2));
        registerSpawns(builder, CACEntities.JUMPING_SPIDER.getKey(), new CACWorldGen.SpawnValues(2, 1, 1));
        registerSpawns(builder, CACEntities.LEAF_INSECT.getKey(), new CACWorldGen.SpawnValues(14, 1, 1));
        registerSpawns(builder, CACEntities.FERRET.getKey(), new CACWorldGen.SpawnValues(3, 2, 3));
        registerSpawns(builder, CACEntities.JUMPING_SPIDER.getKey(), new CACWorldGen.SpawnValues(2, 1, 1));
        registerSpawns(builder, CACEntities.JUMPING_SPIDER.getKey(), new CACWorldGen.SpawnValues(2, 1, 1));
        registerSpawns(builder, CACEntities.SEA_BUNNY.getKey(), new CACWorldGen.SpawnValues(16, 1, 2));
        registerSpawns(builder, CACEntities.DUMBO_OCTOPUS.getKey(), new CACWorldGen.SpawnValues(4, 1, 1));
        registerSpawns(builder, CACEntities.SEA_BUNNY.getKey(), new CACWorldGen.SpawnValues(16, 1, 2));
        registerSpawns(builder, CACEntities.DUMBO_OCTOPUS.getKey(), new CACWorldGen.SpawnValues(4, 1, 1));
        registerSpawns(builder, CACEntities.FERRET.getKey(), new CACWorldGen.SpawnValues(4, 2, 3));
        registerSpawns(builder, CACEntities.FERRET.getKey(), new CACWorldGen.SpawnValues(4, 2, 3));
        registerSpawns(builder, CACEntities.OTTER.getKey(), new CACWorldGen.SpawnValues(1, 3, 5));
        registerSpawns(builder, CACEntities.KOI_FISH.getKey(), new CACWorldGen.SpawnValues(4, 2, 5));
        registerSpawns(builder, CACEntities.DRAGONFLY.getKey(), new CACWorldGen.SpawnValues(7, 1, 1));
        registerSpawns(builder, CACEntities.SHIMA_ENAGA.getKey(), new CACWorldGen.SpawnValues(3, 2, 3));
        registerSpawns(builder, CACEntities.SEA_BUNNY.getKey(), new CACWorldGen.SpawnValues(32, 1, 4));
        registerSpawns(builder, CACEntities.DUMBO_OCTOPUS.getKey(), new CACWorldGen.SpawnValues(8, 1, 1));
        registerSpawns(builder, CACEntities.SEA_BUNNY.getKey(), new CACWorldGen.SpawnValues(16, 1, 4));
        registerSpawns(builder, CACEntities.DUMBO_OCTOPUS.getKey(), new CACWorldGen.SpawnValues(6, 1, 1));
        registerSpawns(builder, CACEntities.SEA_BUNNY.getKey(), new CACWorldGen.SpawnValues(16, 1, 4));
        registerSpawns(builder, CACEntities.DUMBO_OCTOPUS.getKey(), new CACWorldGen.SpawnValues(6, 1, 1));

        builder.pop();
    }

}
