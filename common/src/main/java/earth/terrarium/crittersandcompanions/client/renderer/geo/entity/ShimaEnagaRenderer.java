package earth.terrarium.crittersandcompanions.client.renderer.geo.entity;

import earth.terrarium.crittersandcompanions.client.model.geo.ShimaEnagaModel;
import earth.terrarium.crittersandcompanions.common.entity.ShimaEnagaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ShimaEnagaRenderer extends GeoEntityRenderer<ShimaEnagaEntity> {
    public ShimaEnagaRenderer(EntityRendererProvider.Context context) {
        super(context, new ShimaEnagaModel());
    }
}
