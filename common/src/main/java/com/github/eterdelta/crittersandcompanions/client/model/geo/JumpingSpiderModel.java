package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.entity.JumpingSpiderEntity;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class JumpingSpiderModel extends DefaultedEntityGeoModel<JumpingSpiderEntity> {

    public JumpingSpiderModel() {
        super(CACEntities.JUMPING_SPIDER.getKey().location());
    }

    @Override
    public void setCustomAnimations(JumpingSpiderEntity animatable, long instanceId, AnimationState<JumpingSpiderEntity> animationState) {
        var head = getAnimationProcessor().getBone("head_rotation");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
