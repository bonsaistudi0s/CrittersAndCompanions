package com.github.eterdelta.crittersandcompanions.entity.projectiles;

import com.github.eterdelta.crittersandcompanions.entity.WeevilEntity;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MudBallProjectile extends ThrowableItemProjectile implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MudBallProjectile(EntityType<? extends MudBallProjectile> type, Level level) {
        super(type, level);
    }

    public MudBallProjectile(Level level, LivingEntity owner) {
        super(CACEntities.MUD_BALL.get(), level);
        setOwner(owner);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    private ParticleOptions getParticle() {
        var stack = getItem();
        return stack.isEmpty() ? null : new ItemParticleOption(ParticleTypes.ITEM, stack);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id != 3) {
            return;
        }

        var particle = getParticle();
        if (particle == null) {
            return;
        }

        for (int i = 0; i < 8; ++i) {
            level().addParticle(particle, getX(), getY(), getZ(), 0, 0, 0);
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof WeevilEntity) {
            return;
        }

        var entity = result.getEntity();
        entity.hurt(damageSources().thrown(this, getOwner()), 4);
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (!level().isClientSide()) {
            level().broadcastEntityEvent(this, (byte) 3);
            playSound(SoundEvents.MUD_BREAK, 1, 1);
            discard();
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.MUD;
    }
}
