package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.entity.SnailEntity;
import com.github.eterdelta.crittersandcompanions.entity.brain.behaviour.ClimbingBehaviour;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SnailRenderer extends GeoEntityRenderer<SnailEntity> {

    public SnailRenderer(EntityRendererProvider.Context context, GeoModel<SnailEntity> model) {
        super(context, model);
    }

    @Override
    protected void applyRotations(SnailEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale) {
        if (animatable.behaviour(ClimbingBehaviour.class).isClimbing()) {
            var wallFace = animatable.getClimbingWallFace();
            var newYaw = wallFace == null ? rotationYaw : wallFace.toYRot();
            super.applyRotations(animatable, poseStack, ageInTicks, newYaw, partialTick, nativeScale);
        } else {
            super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick, nativeScale);
        }
    }
}
