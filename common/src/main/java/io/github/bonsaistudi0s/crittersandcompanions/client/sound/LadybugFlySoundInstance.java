package io.github.bonsaistudi0s.crittersandcompanions.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.LadybugEntity;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACSounds;

public class LadybugFlySoundInstance extends AbstractTickableSoundInstance {

    final LadybugEntity ladybug;

    public LadybugFlySoundInstance(LadybugEntity ladybug) {
        super(CACSounds.LADYBUG_FLY_LOOP.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.ladybug = ladybug;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
        this.x = ladybug.getX();
        this.y = ladybug.getY();
        this.z = ladybug.getZ();
    }

    @Override
    public void tick() {
        if (this.ladybug == null || this.ladybug.isRemoved()) {
            this.stop();
            return;
        }

        this.x = (float) this.ladybug.getX();
        this.y = (float) this.ladybug.getY();
        this.z = (float) this.ladybug.getZ();

        if (!ladybug.isFlying()) {
            this.volume = 0.0F;
            return;
        }

        float f = (float) this.ladybug.getDeltaMovement().horizontalDistance();
        if (f >= 0.005F) {
            float currentAttributeSpeed = (float) this.ladybug.getAttributeValue(Attributes.FLYING_SPEED);
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
        return this.ladybug.isBaby() ? 1.1F : 0.7F;
    }

    private float getMaxPitch() {
        return this.ladybug.isBaby() ? 1.5F : 1.1F;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !ladybug.isSilent();
    }
}
