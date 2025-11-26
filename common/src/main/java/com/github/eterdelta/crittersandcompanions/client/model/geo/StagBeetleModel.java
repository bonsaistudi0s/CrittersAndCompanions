package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.LadybugEntity;
import com.github.eterdelta.crittersandcompanions.entity.StabBeetleEntity;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.VariantBehaviour;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.util.function.Function;

public class StagBeetleModel extends DefaultedEntityGeoModel<StabBeetleEntity> {

    private static final ResourceLocation MODEL = CrittersAndCompanions.createId("stab_beetle");
    private static final Function<Integer, ResourceLocation> TEXTURE = Util.memoize(variant -> CrittersAndCompanions.createId("textures/entity/stab_beetle_%s.png".formatted(variant)));

    public StagBeetleModel() {
        super(MODEL);
    }

    @Override
    public ResourceLocation getTextureResource(StabBeetleEntity entity) {
        return TEXTURE.apply(entity.behaviour(VariantBehaviour.class).getVariant() + 1);
    }

}
