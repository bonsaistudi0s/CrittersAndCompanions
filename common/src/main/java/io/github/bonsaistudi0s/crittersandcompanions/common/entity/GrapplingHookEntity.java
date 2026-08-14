package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.OptionalInt;

import io.github.bonsaistudi0s.crittersandcompanions.common.config.CACCommonConfig;
import io.github.bonsaistudi0s.crittersandcompanions.common.extension.IGrapplingState;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.CACPacketHandler;
import io.github.bonsaistudi0s.crittersandcompanions.common.network.ClientboundGrapplingStatePacket;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACEntities;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;

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

        var owner = getOwner();
        if (owner == null) {
            discard();
            return;
        }

        var offsetLengthSqr = distanceToSqr(owner);

        var maxDistance = CACCommonConfig.HANDLER.instance().grapplingHook.maxDistance;
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
        if (isStick) {
            if (offsetLengthSqr > stickLength) {
                var direction = position().subtract(owner.position()).normalize();
                var maxSpeed = CACCommonConfig.HANDLER.instance().grapplingHook.maxSpeed;
                var scale = Math.min(maxSpeed, 0.01D * Math.sqrt(offsetLengthSqr));
                if (scale >= 0) {
                    double oldY = owner.getDeltaMovement().y();
                    owner.setDeltaMovement(owner.getDeltaMovement().add(direction.scale(scale)));
                    double newY = owner.getDeltaMovement().y();
                    owner.hurtMarked = true;
                    if (oldY < 0) {
                        if (newY >= 0) {
                            owner.resetFallDistance();
                        } else if (newY > oldY) {
                            owner.fallDistance *= (float) (newY / oldY);
                        }
                    }
                }
            }
            setDeltaMovement(0.0D, 0.0D, 0.0D);
        }
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

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.isStick) {
            this.setPos(result.getLocation());
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    public void pull() {
        if (getOwner() != null) {
            if (isStick) {
                var pullSpeed = CACCommonConfig.HANDLER.instance().grapplingHook.launchSpeed / 4;
                var maxSpeed = CACCommonConfig.HANDLER.instance().grapplingHook.maxSpeed;
                var direction = position().subtract(getOwner().position()).normalize();
                var distance = distanceTo(getOwner());
                double oldY = getOwner().getDeltaMovement().y();

                var moveVec = direction.scale(Math.min(maxSpeed, pullSpeed * distance));
                var willTravelMostlyHorizontally = Math.abs(getOwner().getY() - this.getY()) <= 1.5D;
                if (willTravelMostlyHorizontally) {
                    moveVec = moveVec.add(0.0D, 0.25D, 0.0D);
                }

                getOwner().setDeltaMovement(moveVec);

                double newY = getOwner().getDeltaMovement().y();
                if (oldY < 0) {
                    if (newY >= 0) {
                        getOwner().resetFallDistance();
                    } else if (newY > oldY) {
                        getOwner().fallDistance *= (float) (newY / oldY);
                    }
                }
            }

            if (!this.level().isClientSide) {
                discard();
            }
        }
    }

    public void updateOwnerState() {
        if (!level().isClientSide() && getOwner() != null
                && getOwner() instanceof Player player
                && getOwner() instanceof IGrapplingState grapplingState) {

            grapplingState.setHook(isAlive() ? this : null);
            CACPacketHandler.sendToTrackingAndSelf(player, new ClientboundGrapplingStatePacket(isAlive() ? OptionalInt.of(getId()) : OptionalInt.empty(), player.getId()));
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
