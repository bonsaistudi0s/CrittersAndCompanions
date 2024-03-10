package earth.terrarium.crittersandcompanions.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class BubbleModel<T extends Player> extends EntityModel<T> {
    public final ModelPart bubble;

    public BubbleModel(ModelPart root) {
        this.bubble = root.getChild("bubble");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bubble = partdefinition.addOrReplaceChild("bubble", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -9.0F, -5.5F, 11.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float wobbleH = Mth.cos(ageInTicks / 4.0F) * 0.2F;
        float wobbleV = Mth.sin(ageInTicks / 4.0F) * 0.1F;
        this.bubble.xScale += wobbleH;
        this.bubble.yScale += wobbleV;
        this.bubble.zScale += wobbleV;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bubble.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}