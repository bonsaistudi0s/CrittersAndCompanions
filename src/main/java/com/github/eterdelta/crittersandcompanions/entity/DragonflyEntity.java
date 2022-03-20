package com.github.eterdelta.crittersandcompanions.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Random;

public class DragonflyEntity extends PathfinderMob implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private BlockPos targetPosition;

    public DragonflyEntity(EntityType<? extends DragonflyEntity> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.OPEN, 1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0D);
    }

    public static boolean checkDragonflySpawnRules(EntityType<DragonflyEntity> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, Random random) {
        return levelAccessor.getBlockState(blockPos.below()).isValidSpawn(levelAccessor, blockPos.below(), entityType) && levelAccessor.getRawBrightness(blockPos, 0) > 8;
    }

    @Override
    protected int getExperienceReward(Player player) {
        return this.random.nextInt(2, 5);
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D));
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos) {
        return this.level.getBiomeName(blockPos).map(biomeResourceKey -> biomeResourceKey == Biomes.RIVER ? 10.0F : 5.0F).orElse(1.0F);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (this.targetPosition != null && (!this.level.isEmptyBlock(this.targetPosition) || this.targetPosition.getY() <= this.level.getMinBuildHeight())) {
            this.targetPosition = null;
        }

        if (this.targetPosition == null || this.random.nextInt(30) == 0 || this.targetPosition.closerThan(this.position(), 2.0D)) {
            Vec3 randomPos = RandomPos.generateRandomPos(this, () -> new BlockPos(this.getX() + (double) this.random.nextInt(7) - (double) this.random.nextInt(7), this.getY() + (double) this.random.nextInt(6) - 2.0D, this.getZ() + (double) this.random.nextInt(7) - (double) this.random.nextInt(7)));
            this.targetPosition = randomPos == null ? this.blockPosition() : new BlockPos(randomPos);
        }

        double d0 = (double) this.targetPosition.getX() + 0.5D - this.getX();
        double d1 = (double) this.targetPosition.getY() + 0.1D - this.getY();
        double d2 = (double) this.targetPosition.getZ() + 0.5D - this.getZ();

        Vec3 deltaMovement = this.getDeltaMovement();
        Vec3 signumDeltaMovement = deltaMovement.add((Math.signum(d0) * 0.5D - deltaMovement.x) * (double) 0.1F, (Math.signum(d1) * (double) 0.7F - deltaMovement.y) * (double) 0.1F, (Math.signum(d2) * 0.5D - deltaMovement.z) * (double) 0.1F);
        this.setDeltaMovement(signumDeltaMovement);

        float angle = (float) (Mth.atan2(signumDeltaMovement.z, signumDeltaMovement.x) * (double) (180F / (float) Math.PI)) - 90.0F;
        float wrappedAngle = Mth.wrapDegrees(angle - this.getYRot());
        this.zza = 0.5F;
        this.setYRot(this.getYRot() + wrappedAngle);
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        return false;
    }

    @Override
    protected void checkFallDamage(double p_20990_, boolean p_20991_, BlockState p_20992_, BlockPos p_20993_) {
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("dragonfly_fly", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
