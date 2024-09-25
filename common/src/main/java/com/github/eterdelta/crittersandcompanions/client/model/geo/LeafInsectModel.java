package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.LeafInsectEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class LeafInsectModel extends DefaultedEntityGeoModel<LeafInsectEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "leaf_insect");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/leaf_insect_1.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/leaf_insect_2.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/leaf_insect_3.png")};

    public LeafInsectModel() {
        super(MODEL);
    }

    @Override
    public ResourceLocation getTextureResource(LeafInsectEntity object) {
        return TEXTURES[object.getVariant()];
    }
}
