package earth.terrarium.crittersandcompanions.client.renderer.geo.entity;

import earth.terrarium.crittersandcompanions.client.model.geo.SeaBunnyModel;
import earth.terrarium.crittersandcompanions.common.entity.SeaBunnyEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SeaBunnyRenderer extends GeoEntityRenderer<SeaBunnyEntity> {
    public SeaBunnyRenderer(EntityRendererProvider.Context context) {
        super(context, new SeaBunnyModel());
    }

    @Override
    public RenderType getRenderType(SeaBunnyEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
