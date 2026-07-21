package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.item.AcornHatItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AcornArmorRenderer extends GeoArmorRenderer<AcornHatItem> {
    public AcornArmorRenderer() {
        super(new DefaultedItemGeoModel<>(CrittersAndCompanions.createId("armor/acorn")));
    }
}
