package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.capability.CACCapabilities;
import com.github.eterdelta.crittersandcompanions.capability.IGrapplingStateCapability;
import com.github.eterdelta.crittersandcompanions.network.CACPacketHandler;
import com.github.eterdelta.crittersandcompanions.network.ClientboundGrapplingStatePacket;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;

public class GrapplingHookEntity extends Projectile {
    protected static final EntityDataAccessor<ItemStack> OWNER_STACK = SynchedEntityData.defineId(GrapplingHookEntity.class, EntityDataSerializers.ITEM_STACK);
    protected boolean isStick;
    protected boolean wasStick;
    protected double stickLength;

    public GrapplingHookEntity(EntityType<? extends GrapplingHookEntity> entityType, Level level) {
        super(entityType, level);
    }

    public GrapplingHookEntity(Player owner, ItemStack ownerStack, Level level) {
        this(CACEntities.GRAPPLING_HOOK.get(), level);
        this.moveTo(owner.getX(), owner.getEyeY(), owner.getZ(), owner.getYHeadRot(), owner.getXRot());
        this.setOwner(owner);
        this.setOwnerStack(ownerStack);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_STACK, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide() && (!this.isFocused() || this.getOwner().distanceToSqr(this) > 1048)) {
            this.discard();
            return;
        }

        AABB collidableBox = this.getBoundingBox().inflate(0.1D);
        Iterable<VoxelShape> collisions = this.level.getBlockCollisions(this, collidableBox);

        isStick = false;
        for (VoxelShape shape : collisions) {
            if (!shape.isEmpty() && shape.bounds().intersects(collidableBox)) {
                isStick = true;
                break;
            }
        }

        if (isStick && !wasStick) {
            stickLength = this.position().subtract(this.getOwner().position()).lengthSqr();
        }

        wasStick = isStick;

        if (isStick && this.getOwner() != null) {
            Vec3 offset = this.position().subtract(this.getOwner().position());
            if (offset.lengthSqr() > stickLength) {
                this.getOwner().setDeltaMovement(this.getOwner().getDeltaMovement().add(offset.scale(0.02D)));
            }
            this.setDeltaMovement(0.0D, 0.0D, 0.0D);
        } else {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
            this.setDeltaMovement(this.getDeltaMovement().scale(0.92D));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.updateOwnerState();
    }

    @Override
    public void remove(RemovalReason removalReason) {
        super.remove(removalReason);
        this.updateOwnerState();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096.0D;
    }

    public void pull() {
        if (this.getOwner() != null) {
            if (this.level.isClientSide() && isStick) {
                this.getOwner().setDeltaMovement(this.position().subtract(this.getOwner().position())
                        .multiply(0.25D, 0.2D, 0.25D)
                        .add(0.0D, 0.25D, 0.0D)
                );
            }
            this.discard();
        }
    }

    public void updateOwnerState() {
        if (!this.level.isClientSide() && this.getOwner() != null && this.getOwner() instanceof Player player) {
            LazyOptional<IGrapplingStateCapability> capability = player.getCapability(CACCapabilities.GRAPPLING_STATE);

            capability.ifPresent(state -> {
                state.setHook(this.isAlive() ? this : null);
                CACPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                        new ClientboundGrapplingStatePacket(this.isAlive() ? Optional.of(this.getId()) : Optional.empty(), player.getId()));
            });
        }
    }

    public boolean isFocused() {
        if (this.getOwner() instanceof Player player) {
            if (this.level.isClientSide()) {
                return player.getMainHandItem().areShareTagsEqual(this.getOwnerStack()) || player.getOffhandItem().areShareTagsEqual(this.getOwnerStack());
            } else {
                return player.getMainHandItem() == this.getOwnerStack() || player.getOffhandItem() == this.getOwnerStack();
            }
        }
        return false;
    }

    public ItemStack getOwnerStack() {
        return this.entityData.get(OWNER_STACK);
    }

    public void setOwnerStack(ItemStack stack) {
        this.entityData.set(OWNER_STACK, stack);
    }
}
