package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.CrittersAndCompanions;
import com.github.eterdelta.crittersandcompanions.registry.CACSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
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

public class ShimaEnagaEntity extends TamableAnimal implements FlyingAnimal, GeoEntity {
    private static final TagKey<Item> FOODS_TAG = ItemTags.create(new ResourceLocation(CrittersAndCompanions.MODID, "shima_enaga_food"));
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public ShimaEnagaEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.FLYING_SPEED, 0.5F).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));
    }

    @Override
    public int getExperienceReward() {
        return this.random.nextInt(2, 6);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingNavigation = new FlyingPathNavigation(this, level);
        flyingNavigation.setCanOpenDoors(false);
        flyingNavigation.setCanFloat(true);
        return flyingNavigation;
    }

    @Override
    public boolean causeFallDamage(float p_148989_, float p_148990_, DamageSource p_148991_) {
        return false;
    }

    @Override
    protected void checkFallDamage(double p_29370_, boolean p_29371_, BlockState p_29372_, BlockPos p_29373_) {
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.6F;
    }

    @Override
    public void travel(Vec3 speed) {
        super.travel(speed);
        Vec3 movement = this.getDeltaMovement();
        if (!this.onGround() && movement.y() < 0.0D) {
            this.setDeltaMovement(movement.multiply(1.0D, 0.5D, 1.0D));
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public Vec3 getLeashOffset() {
        return new Vec3(0.0D, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack handStack = player.getItemInHand(interactionHand);

        if (!this.isTame() && handStack.is(FOODS_TAG)) {
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
                if (handStack.is(FOODS_TAG) && this.getHealth() < this.getMaxHealth()) {
                    this.gameEvent(GameEvent.EAT, this);
                    this.heal(1.0F);
                    if (!player.getAbilities().instabuild) {
                        handStack.shrink(1);
                    }
                } else {
                    this.setOrderedToSit(!this.isOrderedToSit());
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        } else {
            return super.mobInteract(player, interactionHand);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(FOODS_TAG);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return CACSounds.SHIMA_ENAGA_AMBIENT.get();
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> event) {
        if (this.onGround()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("shima_enaga_idle"));
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("shima_enaga_fly"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }
}
