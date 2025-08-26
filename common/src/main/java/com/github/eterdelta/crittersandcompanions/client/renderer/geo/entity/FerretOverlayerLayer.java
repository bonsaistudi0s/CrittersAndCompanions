package com.github.eterdelta.crittersandcompanions.client.renderer.geo.entity;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.api.CACColors;
import com.github.eterdelta.crittersandcompanions.entity.FerretEntity;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class FerretOverlayerLayer extends GeoRenderLayer<FerretEntity> {

    private static final Map<DyeColor, ResourceLocation> TEXTURES = createTextures(false);
    private static final Map<DyeColor, ResourceLocation> BABY_TEXTURES = createTextures(true);

    private static Map<DyeColor, ResourceLocation> createTextures(boolean baby) {
        var base = baby ? "baby_ferret" : "ferret";

        var map = new ImmutableMap.Builder<DyeColor, ResourceLocation>();
        CACColors.supported().forEach(dye -> {
            var id = CrittersAndCompanions.createId("textures/entity/%s_tamed_overlay_%s.png".formatted(base, dye.getSerializedName()));
            map.put(dye, id);
        });
        return map.build();
    }

    public FerretOverlayerLayer(GeoRenderer<FerretEntity> renderer) {
        super(renderer);
    }

    @Override
    @Nullable
    protected ResourceLocation getTextureResource(FerretEntity animatable) {
        var color = animatable.getCollarColor();
        if (color == null) return null;
        var map = animatable.isBaby() ? BABY_TEXTURES : TEXTURES;
        return map.get(color);
    }

    @Override
    public void render(PoseStack poseStack, FerretEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        var texture = getTextureResource(animatable);
        if (texture == null) return;
        renderType = RenderType.entityCutout(texture);
        buffer = bufferSource.getBuffer(renderType);
        var color = getRenderer().getRenderColor(animatable, partialTick, packedLight).argbInt();
        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType, buffer, partialTick, packedLight, packedOverlay, color);
    }
}
