package com.github.eterdelta.crittersandcompanions.client.model.geo;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.FerretEntity;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class FerretModel extends AnimatedGeoModel<FerretEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(CrittersAndCompanions.MODID, "geo/ferret.geo.json");
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/ferret_1.png"),
            new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/ferret_2.png")};
    private static final ResourceLocation ANIMATION = new ResourceLocation(CrittersAndCompanions.MODID, "animations/ferret.animation.json");

    @Override
    public ResourceLocation getModelLocation(FerretEntity object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(FerretEntity object) {
        return TEXTURES[object.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationFileLocation(FerretEntity animatable) {
        return ANIMATION;
    }

    @Override
    public void setLivingAnimations(FerretEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        if (!entity.isSleeping()) {
            IBone neck = this.getAnimationProcessor().getBone("body_2");
            neck.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180.0F));
        }
        
        if (entity.isBaby()) {
            IBone head = this.getAnimationProcessor().getBone("head");
            IBone main = this.getAnimationProcessor().getBone("main");
            head.setScaleX(1.25F);
            head.setScaleY(1.25F);
            head.setScaleZ(1.25F);
            main.setScaleX(0.5F);
            main.setScaleY(0.5F);
            main.setScaleZ(0.5F);
        }
        entity.getFactory().getOrCreateAnimationData(uniqueID).setResetSpeedInTicks(4);
    }
}
