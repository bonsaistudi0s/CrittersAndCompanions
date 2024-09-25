package com.github.eterdelta.crittersandcompanions.client.renderer;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.client.model.BubbleModel;
import com.github.eterdelta.crittersandcompanions.extension.IBubbleState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class BubbleLayer extends RenderLayer<Player, PlayerModel<Player>> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CrittersAndCompanions.MODID, "bubble"), "main");
    private static final ResourceLocation TEXTURE = new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/bubble.png");
    private final BubbleModel<Player> bubbleModel;

    public BubbleLayer(RenderLayerParent<Player, PlayerModel<Player>> parent, EntityModelSet modelSet) {
        super(parent);
        this.bubbleModel = new BubbleModel<>(modelSet.bakeLayer(LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Player player, float limbSwing, float limbSwingAmount, float p_117355_, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!(player instanceof IBubbleState bubbleState)) return;
        if (bubbleState.isBubbleActive()) {
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
            this.bubbleModel.bubble.copyFrom(this.getParentModel().head);
            this.bubbleModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.bubbleModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
