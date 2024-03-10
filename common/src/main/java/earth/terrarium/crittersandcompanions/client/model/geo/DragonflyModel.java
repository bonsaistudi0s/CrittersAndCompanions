package earth.terrarium.crittersandcompanions.client.model.geo;

import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.entity.DragonflyEntity;
import earth.terrarium.crittersandcompanions.common.item.DragonflyArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DragonflyModel extends DefaultedEntityGeoModel<DragonflyEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "dragonfly");

    public DragonflyModel() {
        super(MODEL, false);
    }

    @Override
    public ResourceLocation getTextureResource(DragonflyEntity object) {
        return object.getArmor().isEmpty() ? super.getTextureResource(object) : ((DragonflyArmorItem) object.getArmor().getItem()).getTexture();
    }
}
