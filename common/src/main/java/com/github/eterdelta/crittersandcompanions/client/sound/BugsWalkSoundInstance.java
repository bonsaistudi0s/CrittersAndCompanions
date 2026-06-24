package com.github.eterdelta.crittersandcompanions.client.sound;

import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BugsWalkSoundInstance extends AbstractTickableSoundInstance {

    final LivingEntity entity;

    public BugsWalkSoundInstance(LivingEntity entity) {
        super(CACSounds.BUGS_WALK_LOOP.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.entity = entity;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }

    @Override
    public void tick() {
        if (this.entity == null || this.entity.isRemoved()) {
            this.stop();
            return;
        }

        this.x = (float) this.entity.getX();
        this.y = (float) this.entity.getY();
        this.z = (float) this.entity.getZ();

        if (!entity.onGround()) {
            this.volume = 0.0F;
            return;
        }

        float f = (float) this.entity.getDeltaMovement().horizontalDistance();
        if (f >= 0.005F) {
            float currentAttributeSpeed = (float) this.entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float expectedMaxSpeed = Math.max(0.001F, currentAttributeSpeed * 0.1F);
            float speedRatio = Mth.clamp(f / expectedMaxSpeed, 0.0F, 1.0F);

            this.pitch = Mth.lerp(speedRatio, this.getMinPitch(), this.getMaxPitch());
            this.volume = Mth.lerp(speedRatio, 0.0F, 1.0F);
        } else {
            this.pitch = 0.0F;
            this.volume = 0.0F;
        }
    }

    private float getMinPitch() {
        return this.entity.isBaby() ? 1.1F : 0.7F;
    }

    private float getMaxPitch() {
        return this.entity.isBaby() ? 1.5F : 1.1F;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !entity.isSilent();
    }
}
