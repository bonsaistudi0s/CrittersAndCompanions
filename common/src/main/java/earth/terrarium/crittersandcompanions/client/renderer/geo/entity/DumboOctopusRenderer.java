package earth.terrarium.crittersandcompanions.client.renderer.geo.entity;

import earth.terrarium.crittersandcompanions.client.model.geo.DumboOctopusModel;
import earth.terrarium.crittersandcompanions.common.entity.DumboOctopusEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DumboOctopusRenderer extends GeoEntityRenderer<DumboOctopusEntity> {
    public DumboOctopusRenderer(EntityRendererProvider.Context context) {
        super(context, new DumboOctopusModel());
    }

    @Override
    public RenderType getRenderType(DumboOctopusEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
