package io.github.bonsaistudi0s.crittersandcompanions.common.entity;

import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.Behaviours;
import io.github.bonsaistudi0s.crittersandcompanions.common.entity.brain.behaviour.VariantBehaviour;
import io.github.bonsaistudi0s.crittersandcompanions.common.registry.CACItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class KoiFishEntity extends AbstractSchoolingFish implements GeoEntity {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(KoiFishEntity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public KoiFishEntity(EntityType<? extends KoiFishEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    @Override
    public void registerBehaviours(Behaviours behaviours) {
        behaviours.add(new VariantBehaviour(this, VARIANT, 21));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, OtterEntity.class, 8.0F, 1.7D, 1.4D));
    }

    @Override
    public int getMaxSchoolSize() {
        return 6;
    }

    @Override
    public int getExperienceReward() {
        return this.random.nextInt(1, 4);
    }

    @Override
    public void saveToBucketTag(ItemStack bucketStack) {
        var tag = bucketStack.getOrCreateTag();
        getBehaviours().forEach(it -> it.save(tag));
        super.saveToBucketTag(bucketStack);
    }

    @Override
    public void loadFromBucketTag(CompoundTag bucketCompound) {
        super.loadFromBucketTag(bucketCompound);
        getBehaviours().forEach(it -> it.read(bucketCompound));
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(CACItems.KOI_FISH_BUCKET.get());
    }

    private PlayState predicate(AnimationState<?> event) {
        if (this.isInWater()) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("koi_fish_swim"));
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("koi_fish_on_land"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
