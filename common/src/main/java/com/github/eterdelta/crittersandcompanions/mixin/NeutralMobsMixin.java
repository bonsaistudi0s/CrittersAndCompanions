package com.github.eterdelta.crittersandcompanions.mixin;

import com.github.eterdelta.crittersandcompanions.entity.RedPandaEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {Bee.class, EnderMan.class, IronGolem.class, Llama.class, PolarBear.class, Spider.class, Vex.class, Wolf.class})
public abstract class NeutralMobsMixin extends PathfinderMob {

    protected NeutralMobsMixin(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "registerGoals")
    public void onRegisterGoals(CallbackInfo callback) {
        this.goalSelector.addGoal(-1, new AvoidEntityGoal<>(this, RedPandaEntity.class, 16.0F, 2.0D, 1.5D,
                livingEntity -> ((RedPandaEntity) livingEntity).isAlert() && ((RedPandaEntity) livingEntity).isTame()));
    }
}
