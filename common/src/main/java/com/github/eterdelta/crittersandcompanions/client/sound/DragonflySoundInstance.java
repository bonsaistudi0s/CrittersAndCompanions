package com.github.eterdelta.crittersandcompanions.client.sound;

import com.github.eterdelta.crittersandcompanions.entity.DragonflyEntity;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class DragonflySoundInstance extends AbstractTickableSoundInstance {
    private final DragonflyEntity dragonfly;

    public DragonflySoundInstance(DragonflyEntity dragonflyEntity) {
        super(CACSounds.DRAGONFLY_AMBIENT.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.dragonfly = dragonflyEntity;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.8F;
        this.x = dragonflyEntity.getX();
        this.y = dragonflyEntity.getY();
        this.z = dragonflyEntity.getZ();
    }

    @Override
    public boolean canPlaySound() {
        return !this.dragonfly.isSilent();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (this.dragonfly != null && this.dragonfly.isAlive()) {
            this.x = (float)this.dragonfly.getX();
            this.y = (float)this.dragonfly.getY();
            this.z = (float)this.dragonfly.getZ();
            this.volume = this.dragonfly.isInSittingPose() ? 0.0F : 0.8F;
        } else {
            this.pitch -= 0.05F;
            this.volume -= 0.025F;

            if (volume <= 0) {
                this.stop();
            }
        }
    }
}
