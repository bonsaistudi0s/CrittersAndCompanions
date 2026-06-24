package com.github.eterdelta.crittersandcompanions.client.sound;

import com.github.eterdelta.crittersandcompanions.entity.SnailEntity;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SnailWalkSoundInstance extends AbstractTickableSoundInstance {

    final SnailEntity snail;

    public SnailWalkSoundInstance(SnailEntity snail) {
        super(CACSounds.SNAIL_WALK_LOOP.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.snail = snail;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
        this.x = snail.getX();
        this.y = snail.getY();
        this.z = snail.getZ();
    }

    @Override
    public void tick() {
        if (this.snail == null || this.snail.isRemoved()) {
            this.stop();
            return;
        }

        this.x = (float) this.snail.getX();
        this.y = (float) this.snail.getY();
        this.z = (float) this.snail.getZ();

        if (!snail.onGround()) {
            this.volume = 0.0F;
            return;
        }

        float f = (float) this.snail.getDeltaMovement().horizontalDistance();
        if (f >= 0.005F) {
            float currentAttributeSpeed = (float) this.snail.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float expectedMaxSpeed = Math.max(0.001F, currentAttributeSpeed * 0.1F);
            float speedRatio = Mth.clamp(f / expectedMaxSpeed, 0.0F, 1.0F);

            this.pitch = Mth.lerp(speedRatio, this.getMinPitch(), this.getMaxPitch());
            this.volume = Mth.lerp(speedRatio, 0.0F, 0.5F);
        } else {
            this.pitch = 0.0F;
            this.volume = 0.0F;
        }
    }

    private float getMinPitch() {
        return this.snail.isBaby() ? 1.1F : 0.7F;
    }

    private float getMaxPitch() {
        return this.snail.isBaby() ? 1.5F : 1.1F;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !snail.isSilent();
    }
}
