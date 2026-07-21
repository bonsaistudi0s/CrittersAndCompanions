package io.github.bonsaistudi0s.crittersandcompanions.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.menu.RolyPolyMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import static io.github.bonsaistudi0s.crittersandcompanions.common.menu.RolyPolyMenu.PLAYER_INV_START_Y;

public class RolyPolyScreen extends AbstractContainerScreen<RolyPolyMenu> {

    private static final ResourceLocation TEXTURE =
            CrittersAndCompanions.createId("textures/gui/roly_poly_chest.png");

    private static final int BUTTON_X = 152;
    private static final int BUTTON_Y = 6;
    private static final int BUTTON_WIDTH = 18;
    private static final int BUTTON_HEIGHT = 18;

    private static final ResourceLocation REMOVE_CHEST_SPRITE =
            CrittersAndCompanions.createId("textures/gui/roly_poly/remove_chest.png");
    private static final ResourceLocation REMOVE_CHEST_HIGHLIGHTED_SPRITE =
            CrittersAndCompanions.createId("textures/gui/roly_poly/remove_chest_highlighted.png");

    public RolyPolyScreen(RolyPolyMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelY = PLAYER_INV_START_Y - 11;
        addRenderableWidget(new RemoveChestButton(leftPos + BUTTON_X, topPos + BUTTON_Y, this));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    static class RemoveChestButton extends AbstractButton {

        private final RolyPolyScreen screen;

        RemoveChestButton(int x, int y, RolyPolyScreen screen) {
            super(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, Component.empty());
            this.screen = screen;
        }

        @Override
        public void onPress() {
            var mc = Minecraft.getInstance();
            if (mc.gameMode == null) {
                return;
            }

            mc.gameMode.handleInventoryButtonClick(this.screen.menu.containerId, 0);
            this.screen.onClose();
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation sprite = this.isHovered ? REMOVE_CHEST_HIGHLIGHTED_SPRITE : REMOVE_CHEST_SPRITE;
            RenderSystem.setShaderTexture(0, sprite);
            guiGraphics.blit(sprite, this.getX(), this.getY(), 0.0F, 0.0F, BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }
    }
}
