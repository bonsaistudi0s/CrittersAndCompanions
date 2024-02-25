package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.registry.CACEntities;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class FerretEntity extends TamableAnimal implements GeoEntity {
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DIGGING = SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.INT);
    private static final TagKey<Item> FOODS_TAG = ItemTags.create(new ResourceLocation(CrittersAndCompanions.MODID, "ferret_food"));
    private static final ResourceLocation DIGGABLES = new ResourceLocation(CrittersAndCompanions.MODID, "gameplay/digging");
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    protected BlockState stateToDig;
    protected int digCooldown;

    public FerretEntity(EntityType<? extends FerretEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FerretMoveControl();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.MOVEMENT_SPEED, 0.28D).add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SLEEPING, false);
        this.entityData.define(DIGGING, false);
        this.entityData.define(VARIANT, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new DigGoal());
        this.goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new SleepGoal(200));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, LivingEntity.class, 8.0F, 1.6D, 1.4D, (livingEntity) -> livingEntity.is(this.getLastHurtByMob()) && !livingEntity.is(this.getOwner())));
        this.goalSelector.addGoal(6, new BreedGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new MeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(8, new TemptGoal(this, 1.0D, Ingredient.of(FOODS_TAG), false));
        this.goalSelector.addGoal(9, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(10, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(13, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Animal.class, 10, false, false, (entity) -> entity instanceof Chicken || entity instanceof Rabbit));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Sleeping", this.isSleeping());
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    public int getExperienceReward() {
        return this.random.nextInt(2, 5);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.digCooldown > 0) {
            this.digCooldown--;
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
        FerretEntity baby = CACEntities.FERRET.get().create(level);
        UUID uuid = this.getOwnerUUID();
        if (ageableMob instanceof FerretEntity ferretEntity) {
            if (this.random.nextBoolean()) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(ferretEntity.getVariant());
            }

            if (uuid != null) {
                baby.setOwnerUUID(uuid);
                baby.setTame(true);
            }
        }
        return baby;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (super.doHurtTarget(entity)) {
            this.playSound(CACSounds.BITE_ATTACK.get(), this.getSoundVolume(), this.getVoicePitch());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        if (!this.isSleeping()) {
            ItemStack handStack = player.getItemInHand(interactionHand);

            if (!this.isTame() && handStack.is(Items.RABBIT)) {
                if (!player.getAbilities().instabuild) {
                    handStack.shrink(1);
                }
                if (!this.level().isClientSide()) {
                    if (this.random.nextInt(10) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                        this.tame(player);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            } else if (this.isTame() && this.isOwnedBy(player)) {
                if (!this.level().isClientSide()) {
                    if (handStack.is(Items.CHICKEN) && !this.isBaby() && !this.isInSittingPose()) {
                        if (this.digCooldown <= 0) {
                            this.stateToDig = this.level().getBlockState(this.blockPosition().below());

                            if (stateToDig.is(BlockTags.DIRT) || stateToDig.is(BlockTags.SAND) || stateToDig.is(Tags.Blocks.GRAVEL)) {
                                this.setDigging(true);
                                this.digCooldown = 6000;
                            } else {
                                this.stateToDig = null;
                            }
                        }
                    }
                }
                if (!this.isFood(handStack)) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                } else if (this.getHealth() < this.getMaxHealth()) {
                    this.gameEvent(GameEvent.EAT, this);
                    this.heal(handStack.getFoodProperties(this).getNutrition());
                    if (!player.getAbilities().instabuild) {
                        handStack.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }
            }
            return super.mobInteract(player, interactionHand);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean canFallInLove() {
        return !this.isDigging() && super.canFallInLove();
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(FOODS_TAG);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? null : CACSounds.FERRET_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CACSounds.FERRET_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CACSounds.FERRET_DEATH.get();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor levelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, SpawnGroupData spawnGroupData, CompoundTag p_146750_) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, p_146750_);
        if (mobSpawnType.equals(MobSpawnType.SPAWNER) && this.random.nextFloat() <= 0.2F) {
            for (int i = 0; i < this.random.nextInt(1, 4); i++) {
                FerretEntity baby = CACEntities.FERRET.get().create(this.level());
                baby.setVariant(this.random.nextInt(0, 2));
                baby.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                baby.setBaby(true);
                levelAccessor.addFreshEntity(baby);
            }
        }
        this.setVariant(this.random.nextInt(0, 2));
        return spawnGroupData;
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event) {
        if (this.isDigging()) {
            event.getController().setAnimation(RawAnimation.begin().thenPlayXTimes("ferret_dig", 1));
        } else if (this.isInSittingPose()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("ferret_sit"));
        } else if (this.isSleeping()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("ferret_sleep"));
        } else if (event.isMoving()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("ferret_run"));
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("ferret_idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
        
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    public boolean isDigging() {
        return this.entityData.get(DIGGING);
    }

    public void setDigging(boolean digging) {
        this.entityData.set(DIGGING, digging);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Mth.clamp(variant, 0, 1));
    }



    public class SleepGoal extends Goal {

        private final int countdownTime;
        private int countdown;

        public SleepGoal(int countdownTime) {
            this.countdownTime = countdownTime;
            this.countdown = FerretEntity.this.random.nextInt(reducedTickDelay(countdownTime));
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        }

        public boolean canUse() {
            if (FerretEntity.this.xxa == 0.0F && FerretEntity.this.yya == 0.0F && FerretEntity.this.zza == 0.0F) {
                return this.canSleep() || FerretEntity.this.isSleeping();
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return this.canSleep();
        }

        private boolean canSleep() {
            if (this.countdown > 0) {
                --this.countdown;
                return false;
            } else {
                return FerretEntity.this.level().isNight();
            }
        }

        public void stop() {
            FerretEntity.this.setSleeping(false);
            this.countdown = FerretEntity.this.random.nextInt(this.countdownTime);
        }

        public void start() {
            FerretEntity.this.setInSittingPose(false);
            FerretEntity.this.setJumping(false);
            FerretEntity.this.setSleeping(true);
            FerretEntity.this.getNavigation().stop();
            FerretEntity.this.getMoveControl().setWantedPosition(FerretEntity.this.getX(), FerretEntity.this.getY(), FerretEntity.this.getZ(), 0.0D);
        }
    }

    @Override
    public double getBoneResetTime() {
        return 0;
    }

    public class DigGoal extends Goal {
        protected int digTime;

        public DigGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            return FerretEntity.this.isDigging();
        }

        @Override
        public void start() {
            this.digTime = 35;
        }

        @Override
        public void tick() {
            if (this.digTime > 0) {
                this.digTime--;

                if (this.digTime % 5 == 0 && this.digTime >= 10) {
                    FerretEntity.this.level().playSound(null, FerretEntity.this, SoundEvents.GRAVEL_HIT, SoundSource.BLOCKS, 0.2F, 1.2F);
                    for (int i = 0; i < 4; ++i) {
                        double d0 = FerretEntity.this.random.nextGaussian() * 0.01D;
                        double d1 = FerretEntity.this.random.nextGaussian() * 0.01D;
                        double d2 = FerretEntity.this.random.nextGaussian() * 0.01D;
                        ((ServerLevel) FerretEntity.this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, FerretEntity.this.stateToDig), FerretEntity.this.getX(), FerretEntity.this.getY(), FerretEntity.this.getZ(), 2, d0, d1, d2, 0.1D);
                    }
                }
                if (this.digTime == 10) {
                    LootTable digTable = FerretEntity.this.level().getServer().getLootData().getLootTable(DIGGABLES);
                    LootParams context = new LootParams.Builder((ServerLevel) FerretEntity.this.level()).create(
                            LootContextParamSets.EMPTY);
                    List<ItemStack> dugItems = digTable.getRandomItems(context);

                    if (!dugItems.isEmpty()) {
                        FerretEntity.this.level().playSound(null, FerretEntity.this, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.1F, 1.2F);
                    }

                    for (ItemStack stack : dugItems) {
                        ItemEntity itemEntity = new ItemEntity(FerretEntity.this.level(), FerretEntity.this.getX(), FerretEntity.this.getY(), FerretEntity.this.getZ(), stack);
                        FerretEntity.this.level().addFreshEntity(itemEntity);
                    }
                    ExperienceOrb xp = new ExperienceOrb(FerretEntity.this.level(), FerretEntity.this.getX(), FerretEntity.this.getY(), FerretEntity.this.getZ(), FerretEntity.this.random.nextInt(1, 6));
                    FerretEntity.this.level().addFreshEntity(xp);
                }
            } else {
                this.stop();
            }
        }

        @Override
        public void stop() {
            FerretEntity.this.setDigging(false);
            FerretEntity.this.stateToDig = null;
            this.digTime = 0;
        }
    }

    class FerretMoveControl extends MoveControl {
        public FerretMoveControl() {
            super(FerretEntity.this);
        }

        public void tick() {
            if (!FerretEntity.this.isSleeping()) {
                super.tick();
            }
        }
    }
}
