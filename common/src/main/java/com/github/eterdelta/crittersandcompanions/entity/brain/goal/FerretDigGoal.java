package com.github.eterdelta.crittersandcompanions.entity.brain.goal;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.entity.FerretEntity;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class FerretDigGoal extends Goal {
    private static final ResourceKey<LootTable> DIGGABLES = ResourceKey.create(Registries.LOOT_TABLE, CrittersAndCompanions.createId("gameplay/digging"));

    private final FerretEntity mob;
    protected int digTime;

    public FerretDigGoal(FerretEntity mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        return mob.isDigging();
    }

    @Override
    public void start() {
        this.digTime = 35;
    }

    @Override
    public void tick() {
        var state = mob.getDiggingState();
        if (state == null) return;

        if (this.digTime > 0) {
            this.digTime--;

            if (this.digTime % 5 == 0 && this.digTime >= 10) {

                mob.level().playSound(null, mob, SoundEvents.GRAVEL_HIT, SoundSource.BLOCKS, 0.2F, 1.2F);
                for (int i = 0; i < 4; ++i) {
                    double d0 = mob.getRandom().nextGaussian() * 0.01D;
                    double d1 = mob.getRandom().nextGaussian() * 0.01D;
                    double d2 = mob.getRandom().nextGaussian() * 0.01D;
                    ((ServerLevel) mob.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), mob.getX(), mob.getY(), mob.getZ(), 2, d0, d1, d2, 0.1D);
                }
            }
            if (this.digTime == 10) {
                var digTable = mob.level().getServer().reloadableRegistries().getLootTable(DIGGABLES);
                List<ItemStack> dugItems = digTable.getRandomItems(new LootParams.Builder((ServerLevel) mob.level()).create(LootContextParamSets.EMPTY));

                if (!dugItems.isEmpty()) {
                    mob.level().playSound(null, mob, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.1F, 1.2F);
                }

                for (ItemStack stack : dugItems) {
                    ItemEntity itemEntity = new ItemEntity(mob.level(), mob.getX(), mob.getY(), mob.getZ(), stack);
                    mob.level().addFreshEntity(itemEntity);
                }

                ExperienceOrb xp = new ExperienceOrb(mob.level(), mob.getX(), mob.getY(), mob.getZ(), mob.getRandom().nextInt(1, 6));
                mob.level().addFreshEntity(xp);
            }
        } else {
            this.stop();
        }
    }

    @Override
    public void stop() {
        mob.setDigging(false);
        this.digTime = 0;
    }
}
