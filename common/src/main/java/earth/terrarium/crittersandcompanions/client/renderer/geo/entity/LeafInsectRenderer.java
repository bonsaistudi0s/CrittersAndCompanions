package earth.terrarium.crittersandcompanions.client.renderer.geo.entity;

import earth.terrarium.crittersandcompanions.client.model.geo.LeafInsectModel;
import earth.terrarium.crittersandcompanions.common.entity.LeafInsectEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LeafInsectRenderer extends GeoEntityRenderer<LeafInsectEntity> {
    public LeafInsectRenderer(EntityRendererProvider.Context context) {
        super(context, new LeafInsectModel());
    }

    @Override
    public RenderType getRenderType(LeafInsectEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
