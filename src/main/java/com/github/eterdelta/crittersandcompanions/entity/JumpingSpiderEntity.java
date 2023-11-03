package com.github.eterdelta.crittersandcompanions.entity;

import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class JumpingSpiderEntity extends TamableAnimal implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private PanicGoal panicGoal;

    public JumpingSpiderEntity(EntityType<? extends JumpingSpiderEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new JumpingSpiderMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Spider.createAttributes().add(Attributes.MAX_HEALTH, 14.0D).add(Attributes.ATTACK_DAMAGE, 8.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.panicGoal = new PanicGoal(this, 1.0D);
        this.goalSelector.addGoal(1, this.panicGoal);

        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.0D, Ingredient.of(CACItems.DRAGONFLY_WING.get()), false));
        this.goalSelector.addGoal(6, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Endermite.class, false, (LivingEntity::isAlive)));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, Silverfish.class, false, (LivingEntity::isAlive)));
        this.targetSelector.addGoal(2, new NonTameRandomTargetGoal<>(this, DragonflyEntity.class, false, (LivingEntity::isAlive)));
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.1F, 2.0F);
    }

    @Override
    public void makeStuckInBlock(BlockState blockState, Vec3 p_33797_) {
        if (!blockState.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(blockState, p_33797_);
        }
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack handStack = player.getItemInHand(interactionHand);

        if (!this.isTame() && handStack.is(CACItems.DRAGONFLY_WING.get())) {
            if (!player.getAbilities().instabuild) {
                handStack.shrink(1);
            }
            if (!this.level.isClientSide()) {
                if (this.random.nextInt(10) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide());
        } else if (this.isTame() && this.isOwnedBy(player)) {
            if (!this.level.isClientSide()) {
                if (handStack.is(CACItems.DRAGONFLY_WING.get()) && this.getHealth() < this.getMaxHealth()) {
                    this.gameEvent(GameEvent.EAT, this);
                    this.heal(1.0F);
                    if (!player.getAbilities().instabuild) {
                        handStack.shrink(1);
                    }
                } else {
                    this.setOrderedToSit(!this.isOrderedToSit());
                }
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide());
        } else {
            return super.mobInteract(player, interactionHand);
        }
    }

    @Override
    public void tame(Player player) {
        super.tame(player);
        this.goalSelector.removeGoal(this.panicGoal);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isInSittingPose()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("jumping_spider_sit", ILoopType.EDefaultLoopTypes.LOOP));
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("jumping_spider_walk", ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("jumping_spider_idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 2, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    static class JumpingSpiderMoveControl extends MoveControl {
        private final JumpingSpiderEntity spider;

        public JumpingSpiderMoveControl(JumpingSpiderEntity jumpingSpider) {
            super(jumpingSpider);
            this.spider = jumpingSpider;
        }

        public void tick() {
            if (this.hasWanted() && this.spider.isOnGround() && this.spider.getRandom().nextFloat() <= 0.05F) {
                this.spider.setDeltaMovement(this.spider.getDeltaMovement().add(0.0D, 0.6D, 0.0D));
            }
            super.tick();
        }
    }
}
