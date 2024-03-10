package earth.terrarium.crittersandcompanions.client.renderer.geo.entity;

import earth.terrarium.crittersandcompanions.client.model.geo.RedPandaModel;
import earth.terrarium.crittersandcompanions.common.entity.RedPandaEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RedPandaRenderer extends GeoEntityRenderer<RedPandaEntity> {
    public RedPandaRenderer(EntityRendererProvider.Context context) {
        super(context, new RedPandaModel());
    }

    @Override
    public RenderType getRenderType(RedPandaEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
