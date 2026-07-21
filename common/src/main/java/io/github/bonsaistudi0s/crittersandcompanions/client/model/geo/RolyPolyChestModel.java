package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.RolyPolyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RolyPolyChestModel extends GeoModel<RolyPolyEntity> {

    @Override
    public ResourceLocation getModelResource(RolyPolyEntity object) {
        return CrittersAndCompanions.createId("geo/entity/roly_poly_chest.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RolyPolyEntity object) {
        return CrittersAndCompanions.createId("textures/entity/roly_poly_chest.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RolyPolyEntity animatable) {
        return CrittersAndCompanions.createId("animations/entity/roly_poly.animation.json");
    }
}
