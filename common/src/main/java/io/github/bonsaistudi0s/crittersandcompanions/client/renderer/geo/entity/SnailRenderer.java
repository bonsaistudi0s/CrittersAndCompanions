package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.SnailModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.SnailEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.ClimbingBehaviour;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SnailRenderer extends GeoEntityRenderer<SnailEntity> {

    public SnailRenderer(EntityRendererProvider.Context context) {
        super(context, new SnailModel());
    }

    @Override
    protected void applyRotations(SnailEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        if (animatable.behaviour(ClimbingBehaviour.class).isClimbing()) {
            var wallFace = animatable.getClimbingWallFace();
            var newYaw = wallFace == null ? rotationYaw : wallFace.toYRot();
            super.applyRotations(animatable, poseStack, ageInTicks, newYaw, partialTick);
        } else {
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
        }
    }
}
