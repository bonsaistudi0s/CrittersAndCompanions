package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.KoiFishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class KoiFishModel extends DefaultedEntityGeoModel<KoiFishEntity> {
    private static final ResourceLocation MODEL = CrittersAndCompanions.createId("koi_fish");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            CrittersAndCompanions.createId("textures/entity/koi_fish_1.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_2.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_3.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_4.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_5.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_6.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_7.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_8.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_9.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_10.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_11.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_12.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_13.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_14.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_15.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_16.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_17.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_18.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_19.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_20.png"),
            CrittersAndCompanions.createId("textures/entity/koi_fish_21.png")};

    public KoiFishModel() {
        super(MODEL, false);
    }

    @Override
    public ResourceLocation getTextureResource(KoiFishEntity object) {
        return TEXTURES[object.getVariant()];
    }
}
