package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.LeafInsectEntity;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.VariantBehaviour;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class LeafInsectModel extends DefaultedEntityGeoModel<LeafInsectEntity> {
    private static final ResourceLocation MODEL = CrittersAndCompanions.createId("leaf_insect");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            CrittersAndCompanions.createId("textures/entity/leaf_insect_1.png"),
            CrittersAndCompanions.createId("textures/entity/leaf_insect_2.png"),
            CrittersAndCompanions.createId("textures/entity/leaf_insect_3.png")};

    public LeafInsectModel() {
        super(MODEL);
    }

    @Override
    public ResourceLocation getTextureResource(LeafInsectEntity entity) {
        return TEXTURES[entity.behaviour(VariantBehaviour.class).getVariant()];
    }

    @Override
    public void setCustomAnimations(LeafInsectEntity animatable, long instanceId, AnimationState<LeafInsectEntity> animationState) {
        var head = getAnimationProcessor().getBone("head_rotation");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }

}
