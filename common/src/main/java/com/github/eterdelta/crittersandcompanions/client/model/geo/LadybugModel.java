package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.LadybugEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class LadybugModel extends DefaultedEntityGeoModel<LadybugEntity> {

    private static final ResourceLocation MODEL = CrittersAndCompanions.createId("ladybug");
    private static final ResourceLocation TEXTURE = CrittersAndCompanions.createId("textures/entity/ladybug.png");

    public LadybugModel() {
        super(MODEL);
    }

    @Override
    public ResourceLocation getTextureResource(LadybugEntity entity) {
        return TEXTURE;
    }

}
