package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.VariantGeoModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.KoiFishEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class KoiFishRenderer extends GeoEntityRenderer<KoiFishEntity> {

    public KoiFishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VariantGeoModel<>(CrittersAndCompanions.createId("koi_fish")));
    }
}
