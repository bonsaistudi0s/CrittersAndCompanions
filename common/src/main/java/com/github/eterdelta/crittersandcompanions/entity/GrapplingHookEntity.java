package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.extension.IGrapplingState;
import com.github.eterdelta.crittersandcompanions.network.CACPacketHandler;
import com.github.eterdelta.crittersandcompanions.network.ClientboundGrapplingStatePacket;
import com.github.eterdelta.crittersandcompanions.platform.Services;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import java.util.OptionalInt;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrapplingHookEntity extends ThrowableItemProjectile {
    protected boolean isStick;
    protected double stickLength;
    private boolean addedToWorld;

    public GrapplingHookEntity(EntityType<? extends GrapplingHookEntity> entityType, Level level) {
        super(entityType, level);
    }

    public GrapplingHookEntity(Player owner, ItemStack ownerStack, Level level) {
        this(CACEntities.GRAPPLING_HOOK.get(), level);
        this.moveTo(owner.getX(), owner.getEyeY(), owner.getZ(), owner.getYHeadRot(), owner.getXRot());
        this.setOwner(owner);
        this.setItem(ownerStack);
    }

    @Override
    protected Item getDefaultItem() {
        return CACItems.GRAPPLING_HOOK.get();
    }

    @Override
    public void tick() {
        super.tick();

        if (!addedToWorld) {
            updateOwnerState();
            addedToWorld = true;
        }

        if (getOwner() == null) {
            discard();
            return;
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
                    new ClientboundGrapplingStatePacket(isAlive() ? OptionalInt.of(getId()) : OptionalInt.empty(), player.getId()));
        }
    }

    public boolean isFocused() {
        if (getOwner() instanceof Player player) {
            return ItemStack.isSameItemSameComponents(player.getMainHandItem(), getItem())
                    || ItemStack.isSameItemSameComponents(player.getOffhandItem(), getItem());
        }
        return false;
    }

}
