package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import com.github.eterdelta.crittersandcompanions.item.DragonflyArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DragonflyModel extends DefaultedEntityGeoModel<DragonflyEntity> {

    public DragonflyModel(ResourceLocation id) {
        super(id);
    }

    @Override
    public ResourceLocation getTextureResource(DragonflyEntity object) {
        if (object.getArmor().getItem() instanceof DragonflyArmorItem item) {
            return item.getTexture();
        }

        return super.getTextureResource(object);
    }
}
