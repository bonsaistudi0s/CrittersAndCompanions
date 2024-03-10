package earth.terrarium.crittersandcompanions.client.model.geo;

import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.entity.JumpingSpiderEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class JumpingSpiderModel extends DefaultedEntityGeoModel<JumpingSpiderEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "jumping_spider");

    public JumpingSpiderModel() {
        super(MODEL);
    }

    @Override
    public void setCustomAnimations(JumpingSpiderEntity animatable, long instanceId, AnimationState<JumpingSpiderEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head_rotation");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
