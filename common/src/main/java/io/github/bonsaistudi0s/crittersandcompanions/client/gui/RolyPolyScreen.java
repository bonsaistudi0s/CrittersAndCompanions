package io.github.bonsaistudi0s.crittersandcompanions.client.gui;

import static io.github.bonsaistudi0s.crittersandcompanions.common.menu.RolyPolyMenu.PLAYER_INV_START_Y;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import org.jetbrains.annotations.NotNull;

import io.github.bonsaistudi0s.crittersandcompanions.CrittersAndCompanions;
import io.github.bonsaistudi0s.crittersandcompanions.common.menu.RolyPolyMenu;

public class RolyPolyScreen extends AbstractContainerScreen<RolyPolyMenu> {

    private static final ResourceLocation TEXTURE =
            CrittersAndCompanions.createId("textures/gui/roly_poly_chest.png");

    private static final int BUTTON_X = 152;
    private static final int BUTTON_Y = 6;
    private static final int BUTTON_WIDTH = 18;
    private static final int BUTTON_HEIGHT = 18;

    private static final WidgetSprites REMOVE_CHEST_SPRITES = new WidgetSprites(
            CrittersAndCompanions.createId("roly_poly/remove_chest"),
            CrittersAndCompanions.createId("roly_poly/remove_chest_highlighted")
    );

    public RolyPolyScreen(RolyPolyMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        inventoryLabelY = PLAYER_INV_START_Y - 11;
        addRenderableWidget(new ImageButton(
                leftPos + BUTTON_X, topPos + BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT,
                REMOVE_CHEST_SPRITES,
                btn -> {
                    var mc = Minecraft.getInstance();
                    if (mc.gameMode == null) {
                        return;
                    }

                    mc.gameMode.handleInventoryButtonClick(menu.containerId, 0);
                    this.onClose();
                }
        ));
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
}
