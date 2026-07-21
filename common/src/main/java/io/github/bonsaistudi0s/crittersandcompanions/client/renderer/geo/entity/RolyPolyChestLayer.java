package io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.client.model.geo.RolyPolyChestModel;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.RolyPolyEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.ChestBehaviour;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class RolyPolyChestLayer extends GeoRenderLayer<RolyPolyEntity> {

    private static final ResourceLocation TEXTURE = CrittersAndCompanions.createId("textures/entity/roly_poly_chest.png");
    private final GeoModel<RolyPolyEntity> chestModel;

    public RolyPolyChestLayer(GeoRenderer<RolyPolyEntity> renderer) {
        super(renderer);
        this.chestModel = new RolyPolyChestModel();
    }

    @Override
    public void render(PoseStack poseStack, RolyPolyEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!animatable.behaviour(ChestBehaviour.class).hasChest()) {
            return;
        }

        var chestBakedModel = chestModel.getBakedModel(chestModel.getModelResource(animatable));

        // sync bones with main model's body
        for (var chestBone : chestModel.getAnimationProcessor().getRegisteredBones()) {
            bakedModel.getBone(chestBone.getName()).ifPresent(mainBone -> {
                chestBone.setPosX(mainBone.getPosX());
                chestBone.setPosY(mainBone.getPosY());
                chestBone.setPosZ(mainBone.getPosZ());
                chestBone.setRotX(mainBone.getRotX());
                chestBone.setRotY(mainBone.getRotY());
                chestBone.setRotZ(mainBone.getRotZ());
                chestBone.setScaleX(mainBone.getScaleX());
                chestBone.setScaleY(mainBone.getScaleY());
                chestBone.setScaleZ(mainBone.getScaleZ());
            });
        }

        renderType = RenderType.entityCutoutNoCull(TEXTURE);
        buffer = bufferSource.getBuffer(renderType);
        var color = getRenderer().getRenderColor(animatable, partialTick, packedLight);
        getRenderer().reRender(chestBakedModel, poseStack, bufferSource, animatable, renderType, buffer, partialTick, packedLight, packedOverlay, color.getRedFloat(), color.getGreenFloat(), color.getBlueFloat(), color.getAlphaFloat());
    }
}
