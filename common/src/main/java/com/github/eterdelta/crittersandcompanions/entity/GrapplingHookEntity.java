package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.extension.IGrapplingState;
import com.github.eterdelta.crittersandcompanions.network.CACPacketHandler;
import com.github.eterdelta.crittersandcompanions.network.ClientboundGrapplingStatePacket;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import java.util.Optional;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrapplingHookEntity extends Projectile {
    protected static final EntityDataAccessor<ItemStack> OWNER_STACK = SynchedEntityData.defineId(GrapplingHookEntity.class, EntityDataSerializers.ITEM_STACK);
    protected boolean isStick;
    protected double stickLength;
    private boolean addedToWorld;

    public GrapplingHookEntity(EntityType<? extends GrapplingHookEntity> entityType, Level level) {
        super(entityType, level);
    }

    public GrapplingHookEntity(Player owner, ItemStack ownerStack, Level level) {
        this(CACEntities.GRAPPLING_HOOK.get(), level);
        moveTo(owner.getX(), owner.getEyeY(), owner.getZ(), owner.getYHeadRot(), owner.getXRot());
        setOwner(owner);
        setOwnerStack(ownerStack);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(OWNER_STACK, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();

        if (!addedToWorld) {
            updateOwnerState();
            addedToWorld = true;
        }

        var offsetLengthSqr = distanceToSqr(getOwner());

        var maxDistance = Services.CONFIGS.common().grapplingHookMaxDistance.get();
        var maxDistanceSqr = maxDistance * maxDistance;
        if (!level().isClientSide() && (!isFocused() || offsetLengthSqr > maxDistanceSqr)) {
            discard();
            return;
        }

        var collidableBox = getBoundingBox().inflate(0.25D);
        var collisions = level().getBlockCollisions(this, collidableBox);

        var willStick = false;
        for (VoxelShape shape : collisions) {
            if (!shape.isEmpty() && shape.bounds().intersects(collidableBox)) {
                willStick = true;
                break;
            }
        }

        if (willStick && !isStick) {
            stickLength = offsetLengthSqr;
            playSound(SoundEvents.SLIME_SQUISH);
        }

        isStick = willStick;
        if (isStick && getOwner() != null) {
            if (offsetLengthSqr > stickLength) {
                var direction = position().subtract(getOwner().position()).normalize();
                var maxSpeed = Services.CONFIGS.common().grapplingHookMaxSpeed.get();
                var scale = Math.min(maxSpeed, 0.01D * Math.sqrt(offsetLengthSqr));
                if (scale >= 0) {
                    getOwner().setDeltaMovement(getOwner().getDeltaMovement().add(direction.scale(scale)));
                    getOwner().hurtMarked = true;
                }
            }
            setDeltaMovement(0.0D, 0.0D, 0.0D);
        } else {
            setDeltaMovement(getDeltaMovement().scale(0.98D));
            setDeltaMovement(getDeltaMovement().add(0.0D, -0.03D, 0.0D));
        }
        move(MoverType.SELF, getDeltaMovement());
    }

    @Override
    public void remove(RemovalReason removalReason) {
        super.remove(removalReason);
        updateOwnerState();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096.0D;
    }

    public void pull() {
        if (getOwner() != null) {
            if (isStick) {
                var pullSpeed = Services.CONFIGS.common().grapplingHookSpeed.get() / 4;
                var maxSpeed = Services.CONFIGS.common().grapplingHookMaxSpeed.get();
                var direction = position().subtract(getOwner().position()).normalize();
                var distance = distanceTo(getOwner());
                getOwner().setDeltaMovement(direction.scale(Math.min(maxSpeed, pullSpeed * distance)));
            }
            discard();
        }
    }

    public void updateOwnerState() {
        if (!level().isClientSide() && getOwner() != null
                && getOwner() instanceof Player player
                && getOwner() instanceof IGrapplingState grapplingState) {

            grapplingState.setHook(isAlive() ? this : null);
            CACPacketHandler.GRAPPLING_STATE.sendToTracking(player,
                    new ClientboundGrapplingStatePacket(isAlive() ? Optional.of(getId()) : Optional.empty(), player.getId()));
        }
    }

    public boolean isFocused() {
        if (getOwner() instanceof Player player) {
            return ItemStack.isSameItemSameTags(player.getMainHandItem(), getOwnerStack())
                    || ItemStack.isSameItemSameTags(player.getOffhandItem(), getOwnerStack());
        }
        return false;
    }

    public ItemStack getOwnerStack() {
        return entityData.get(OWNER_STACK);
    }

    public void setOwnerStack(ItemStack stack) {
        entityData.set(OWNER_STACK, stack);
    }
}
