package earth.terrarium.crittersandcompanions.client.model.geo;

import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.entity.ShimaEnagaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ShimaEnagaModel extends DefaultedEntityGeoModel<ShimaEnagaEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "shima_enaga");

    public ShimaEnagaModel() {
        super(MODEL, false);
    }
}
