package com.github.eterdelta.crittersandcompanions.client.renderer.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.item.AcornHatItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AcornArmorRenderer extends GeoArmorRenderer<AcornHatItem> {
    public AcornArmorRenderer() {
        super(new DefaultedItemGeoModel<>(CrittersAndCompanions.createId("armor/acorn")));
    }
}
