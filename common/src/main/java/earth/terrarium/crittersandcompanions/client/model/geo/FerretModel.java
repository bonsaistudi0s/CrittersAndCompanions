package earth.terrarium.crittersandcompanions.client.model.geo;

import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.common.entity.FerretEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FerretModel extends GeoModel<FerretEntity> {
    private static final ResourceLocation[] MODELS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/entity/ferret.geo.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "geo/entity/baby_ferret.geo.json")};
    private static final ResourceLocation[] ADULT_TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/ferret_1.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/ferret_2.png")};
    private static final ResourceLocation[] BABY_TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/baby_ferret_1.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/baby_ferret_2.png")};
    private static final ResourceLocation[] ANIMATIONS = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/entity/ferret.animation.json"),
            new ResourceLocation(CrittersAndCompanions.MODID, "animations/entity/baby_ferret.animation.json")};

    @Override
    public ResourceLocation getModelResource(FerretEntity object) {
        return MODELS[object.isBaby() ? 1 : 0];
    }

    @Override
    public ResourceLocation getTextureResource(FerretEntity object) {
        return object.isBaby() ? BABY_TEXTURES[object.getVariant()] : ADULT_TEXTURES[object.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(FerretEntity animatable) {
        return ANIMATIONS[animatable.isBaby() ? 1 : 0];
    }

    @Override
    public void setCustomAnimations(FerretEntity animatable, long instanceId, AnimationState<FerretEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        EntityModelData data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (!animatable.isSleeping()) {
            CoreGeoBone neck = this.getAnimationProcessor().getBone("body_2");
            neck.setRotX(data.headPitch() * Mth.DEG_TO_RAD);
            neck.setRotY(data.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}