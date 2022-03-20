package com.github.eterdelta.crittersandcompanions.registry;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import com.github.eterdelta.crittersandcompanions.entity.KoiFishEntity;
import com.github.eterdelta.crittersandcompanions.entity.OtterEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CaCEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, CrittersAndCompanions.MODID);

    public static final RegistryObject<EntityType<OtterEntity>> OTTER = ENTITIES.register("otter", () -> EntityType.Builder.of(OtterEntity::new, MobCategory.WATER_CREATURE).sized(0.8F, 0.4F).build("otter"));
    public static final RegistryObject<EntityType<KoiFishEntity>> KOI_FISH = ENTITIES.register("koi_fish", () -> EntityType.Builder.of(KoiFishEntity::new, MobCategory.WATER_AMBIENT).sized(0.6F, 0.3F).build("koi_fish"));
    public static final RegistryObject<EntityType<DragonflyEntity>> DRAGONFLY = ENTITIES.register("dragonfly", () -> EntityType.Builder.of(DragonflyEntity::new, MobCategory.AMBIENT).sized(0.8F, 0.2F).build("dragonfly"));
}