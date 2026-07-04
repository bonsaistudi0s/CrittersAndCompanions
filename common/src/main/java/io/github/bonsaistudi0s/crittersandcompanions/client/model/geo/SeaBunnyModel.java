package io.github.bonsaistudi0s.crittersandcompanions.client.model.geo;

import net.minecraft.resources.ResourceLocation;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.SeaBunnyEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.ClimbingBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.VariantBehaviour;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SeaBunnyModel extends DefaultedEntityGeoModel<SeaBunnyEntity> {
    private final ResourceLocation[] textures;

    public SeaBunnyModel(ResourceLocation id) {
        super(id);
        this.textures = new ResourceLocation[]{
                buildFormattedTexturePath(id.withSuffix("_white")),
                buildFormattedTexturePath(id.withSuffix("_blue")),
                buildFormattedTexturePath(id.withSuffix("_yellow"))
        };
    }

    @Override
    public ResourceLocation getTextureResource(SeaBunnyEntity object) {
        return textures[object.behaviour(VariantBehaviour.class).getVariant()];
    }

    @Override
    public void setCustomAnimations(SeaBunnyEntity animatable, long instanceId, AnimationState<SeaBunnyEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        EntityModelData data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        var mainBone = this.getAnimationProcessor().getBone("main");

        mainBone.setRotY(data.netHeadYaw() * ((float) Math.PI / 180.0F));
        if (animatable.behaviour(ClimbingBehaviour.class).isClimbing()) {
            mainBone.setRotX(90.0F * ((float) Math.PI / 180.0F));
        } else {
            mainBone.setRotX(0F);
        }
    }
}
