package earth.terrarium.crittersandcompanions.client.renderer.geo.entity;

import earth.terrarium.crittersandcompanions.client.model.geo.FerretModel;
import earth.terrarium.crittersandcompanions.common.entity.FerretEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FerretRenderer extends GeoEntityRenderer<FerretEntity> {
    public FerretRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FerretModel());
    }

    @Override
    public RenderType getRenderType(FerretEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
