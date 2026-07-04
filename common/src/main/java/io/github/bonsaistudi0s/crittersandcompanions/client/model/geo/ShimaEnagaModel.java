package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import net.minecraft.util.Mth;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.ShimaEnagaEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ShimaEnagaModel extends DefaultedEntityGeoModel<ShimaEnagaEntity> {

    public ShimaEnagaModel() {
        super(CrittersAndCompanions.createId("shima_enaga"));
    }

    @Override
    public void setCustomAnimations(ShimaEnagaEntity animatable, long instanceId, AnimationState<ShimaEnagaEntity> animationState) {
        var head = getAnimationProcessor().getBone("head_rotation");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX((entityData.headPitch() + 10F) * Mth.DEG_TO_RAD * 0.6F);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD * 0.6F);
        }
    }

}
