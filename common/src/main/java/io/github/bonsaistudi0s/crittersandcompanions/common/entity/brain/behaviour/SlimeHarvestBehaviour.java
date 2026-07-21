package io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour;

import net.minecraft.nbt.CompoundTag;

public class SlimeHarvestBehaviour implements Behaviour {

    public static final int HARVEST_COOLDOWN = 20 * 60 * 5;
    private static final String TAG_COOLDOWN = "SlimeHarvestCooldown";

    private int cooldown = 0;

    public SlimeHarvestBehaviour() {
    }

    @Override
    public void serverTick() {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    @Override
    public void save(CompoundTag nbt) {
        nbt.putInt(TAG_COOLDOWN, cooldown);
    }

    @Override
    public void read(CompoundTag nbt) {
        cooldown = nbt.getInt(TAG_COOLDOWN);
    }

    public boolean isReady() {
        return cooldown <= 0;
    }

    public void startCooldown() {
        this.cooldown = HARVEST_COOLDOWN;
    }
}
