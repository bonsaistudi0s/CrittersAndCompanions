package com.github.eterdelta.crittersandcompanions.menu;

import com.github.eterdelta.crittersandcompanions.registry.CACMenuTypes;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RolyPolyMenu extends AbstractContainerMenu {

    public static final int SLOTS = 10;

    private static final int ENTITY_COLS = 5;
    private static final int ENTITY_ROWS = 2;
    private static final int ENTITY_START_X = 44;
    private static final int ENTITY_START_Y = 20;

    private static final int PLAYER_INV_START_X = 8;
    public static final int PLAYER_INV_START_Y = 69;
    private static final int HOTBAR_START_Y = 127;

    private final @Nullable Runnable onRemoveChest;

    public RolyPolyMenu(int containerId, Inventory playerInventory, SimpleContainer container, @Nullable Runnable onRemoveChest) {
        super(CACMenuTypes.ROLY_POLY_CHEST.get(), containerId);
        checkContainerSize(container, SLOTS);
        this.onRemoveChest = onRemoveChest;

        // entity inventory
        for (int row = 0; row < ENTITY_ROWS; row++) {
            for (int col = 0; col < ENTITY_COLS; col++) {
                addSlot(new Slot(container,
                        col + row * ENTITY_COLS,
                        ENTITY_START_X + col * 18,
                        ENTITY_START_Y + row * 18));
            }
        }

        // player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory,
                        col + row * 9 + 9,
                        PLAYER_INV_START_X + col * 18,
                        PLAYER_INV_START_Y + row * 18));
            }
        }

        // hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col,
                    PLAYER_INV_START_X + col * 18,
                    HOTBAR_START_Y));
        }
    }

    @Override
    public boolean clickMenuButton(@NotNull Player player, int id) {
        if (id == 0 && onRemoveChest != null) {
            onRemoveChest.run();
            return true;
        }
        return false;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        var result = ItemStack.EMPTY;
        var slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            result = slotStack.copy();

            var fromEntitySlots = index < SLOTS;
            if (fromEntitySlots) {
                if (!moveItemStackTo(slotStack, SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!moveItemStackTo(slotStack, 0, SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }
}
