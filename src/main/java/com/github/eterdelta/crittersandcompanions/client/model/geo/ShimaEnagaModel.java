package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.LeafInsectEntity;
import com.github.eterdelta.crittersandcompanions.entity.ShimaEnagaEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ShimaEnagaModel extends DefaultedEntityGeoModel<ShimaEnagaEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "shima_enaga");

    public ShimaEnagaModel() {
        super(MODEL, false);
    }

    @Override
    public void setCustomAnimations(ShimaEnagaEntity animatable, long instanceId, AnimationState<ShimaEnagaEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head_rotation");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX((entityData.headPitch() + 10F) * Mth.DEG_TO_RAD * 0.6F);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD * 0.6F);
        }
    }
}
