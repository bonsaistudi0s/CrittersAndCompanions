package earth.terrarium.crittersandcompanions.client.renderer.geo.entity;

import earth.terrarium.crittersandcompanions.client.model.geo.DragonflyModel;
import earth.terrarium.crittersandcompanions.common.entity.DragonflyEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DragonflyRenderer extends GeoEntityRenderer<DragonflyEntity> {
    public DragonflyRenderer(EntityRendererProvider.Context context) {
        super(context, new DragonflyModel());
    }
}
