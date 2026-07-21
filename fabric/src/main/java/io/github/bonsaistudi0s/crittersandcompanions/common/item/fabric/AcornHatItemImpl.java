package io.github.bonsaistudi0s.crittersandcompanions.common.item.fabric;

import io.github.bonsaistudi0s.crittersandcompanions.client.renderer.geo.AcornArmorRenderer;
import io.github.bonsaistudi0s.crittersandcompanions.common.item.AcornHatItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class AcornHatItemImpl extends AcornHatItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public AcornHatItemImpl(Properties properties) {
        super(properties);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {

            private GeoArmorRenderer<?> renderer;

            @Override
            public HumanoidModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<LivingEntity> original) {
                if (renderer == null) {
                    renderer = new AcornArmorRenderer();
                }

                renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                //noinspection unchecked
                return renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
