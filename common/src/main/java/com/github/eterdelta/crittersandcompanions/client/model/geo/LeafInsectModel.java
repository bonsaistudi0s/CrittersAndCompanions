package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.LeafInsectEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

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

    @Override
    public void setCustomAnimations(LeafInsectEntity animatable, long instanceId, AnimationState<LeafInsectEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head_rotation");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }

}
