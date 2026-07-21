package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour;

import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ChestBehaviour implements Behaviour {

    @FunctionalInterface
    public interface MenuFactory {
        AbstractContainerMenu create(int containerId, Inventory playerInventory, SimpleContainer container);
    }

    private final TamableAnimal owner;
    private final EntityDataAccessor<Boolean> dataAccessor;
    private final int slots;
    private final MenuFactory menuFactory;
    private SimpleContainer inventory;
    private @Nullable Player viewer = null;

    public ChestBehaviour(TamableAnimal owner, EntityDataAccessor<Boolean> dataAccessor, int slots, MenuFactory menuFactory) {
        this.owner = owner;
        this.dataAccessor = dataAccessor;
        this.slots = slots;
        this.menuFactory = menuFactory;
        this.inventory = new SimpleContainer(slots);
    }

    public boolean hasChest() {
        return owner.getEntityData().get(dataAccessor);
    }

    public void setHasChest(boolean value) {
        owner.getEntityData().set(dataAccessor, value);
        owner.refreshDimensions();
    }

    public boolean isBeingAccessed() {
        return viewer != null;
    }

    @Override
    public void defineSyncedData(SynchedEntityData entityData) {
        entityData.define(dataAccessor, false);
    }

    @Override
    public void serverTick() {
        if (viewer != null && viewer.containerMenu == viewer.inventoryMenu) {
            viewer = null;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!owner.isTame() || !owner.isOwnedBy(player)) {
            return InteractionResult.PASS;
        }

        if (player.isSecondaryUseActive()) {
            if (hasChest()) {
                if (!owner.level().isClientSide()) {
                    viewer = player;
                    var title = owner.getDisplayName();
                    var inv = inventory;
                    player.openMenu(new SimpleMenuProvider(
                            (id, playerInv, p) -> menuFactory.create(id, playerInv, inv),
                            title
                    ));
                }
                return InteractionResult.sidedSuccess(owner.level().isClientSide());
            }
            return InteractionResult.PASS;
        }

        var stack = player.getItemInHand(hand);
        if (!hasChest() && stack.is(CACTags.WOODEN_CHESTS) && !owner.isBaby()) {
            if (!owner.level().isClientSide()) {
                setHasChest(true);
                owner.playSound(
                        SoundEvents.DONKEY_CHEST,
                        1.0F,
                        (owner.getRandom().nextFloat() - owner.getRandom().nextFloat()) * 0.2F + 1.0F
                );
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(owner.level().isClientSide());
        }

        return InteractionResult.PASS;
    }

    @Override
    public void save(CompoundTag nbt) {
        nbt.putBoolean("HasChest", hasChest());
        if (hasChest()) {
            var items = new ListTag();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                var stack = inventory.getItem(i);
                if (!stack.isEmpty()) {
                    var item = new CompoundTag();
                    item.putByte("Slot", (byte) i);
                    items.add(stack.save(item));
                }
            }
            nbt.put("ChestItems", items);
        }
    }

    @Override
    public void read(CompoundTag nbt) {
        setHasChest(nbt.getBoolean("HasChest"));
        inventory = new SimpleContainer(slots);
        if (hasChest()) {
            var items = nbt.getList("ChestItems", 10);
            for (int i = 0; i < items.size(); i++) {
                var item = items.getCompound(i);
                int slot = item.getByte("Slot") & 0xFF;
                if (slot < inventory.getContainerSize()) {
                    inventory.setItem(slot, ItemStack.of(item));
                }
            }
        }
    }

    @Override
    public void dropEquipment() {
        if (hasChest() && !owner.level().isClientSide()) {
            owner.spawnAtLocation(Items.CHEST);
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                var stack = inventory.getItem(i);
                if (!stack.isEmpty()) {
                    owner.spawnAtLocation(stack);
                }
            }
            setHasChest(false);
            inventory = new SimpleContainer(slots);
            viewer = null;
        }
    }
}
