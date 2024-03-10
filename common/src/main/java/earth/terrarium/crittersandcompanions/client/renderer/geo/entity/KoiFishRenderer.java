package earth.terrarium.crittersandcompanions.client.renderer.geo.entity;

import earth.terrarium.crittersandcompanions.client.model.geo.KoiFishModel;
import earth.terrarium.crittersandcompanions.common.entity.KoiFishEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class KoiFishRenderer extends GeoEntityRenderer<KoiFishEntity> {
    public KoiFishRenderer(EntityRendererProvider.Context context) {
        super(context, new KoiFishModel());
    }

    @Override
    public RenderType getRenderType(KoiFishEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
