package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.DumboOctopusEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DumboOctopusModel extends DefaultedEntityGeoModel<DumboOctopusEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "dumbo_octopus");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/dumbo_octopus_1.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/dumbo_octopus_2.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/dumbo_octopus_3.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/dumbo_octopus_4.png")};

    public DumboOctopusModel() {
        super(MODEL, false);
    }

    @Override
    public ResourceLocation getTextureResource(DumboOctopusEntity object) {
        return TEXTURES[object.getVariant()];
    }
}
