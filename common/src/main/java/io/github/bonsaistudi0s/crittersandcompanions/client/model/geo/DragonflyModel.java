package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import net.minecraft.resources.ResourceLocation;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.DragonflyEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.item.DragonflyArmorItem;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DragonflyModel extends DefaultedEntityGeoModel<DragonflyEntity> {

    public DragonflyModel() {
        super(CrittersAndCompanions.createId("dragonfly"));
    }

    @Override
    public ResourceLocation getTextureResource(DragonflyEntity object) {
        if (object.getArmor().getItem() instanceof DragonflyArmorItem item) {
            return item.getTexture();
        }

        return super.getTextureResource(object);
    }
}
