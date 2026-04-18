package com.github.eterdelta.crittersandcompanions;

import com.github.eterdelta.crittersandcompanions.registry.CACItems;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class OakLeavesAcornModifier extends LootModifier {

    // same as vanilla apple drop chance
    private static final float[] FORTUNE_CHANCES = {
            0.005f,
            0.0055555557f,
            0.00625f,
            0.008333334f,
            0.025f
    };

    public static final MapCodec<OakLeavesAcornModifier> CODEC = MapCodec.unit(OakLeavesAcornModifier::new);

    protected OakLeavesAcornModifier() {
        super(new LootItemCondition[0]);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> drops, LootContext context) {
        var tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool != null) {
            boolean isSilkTouch = tool.getEnchantmentLevel(
                    context.getLevel().registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(Enchantments.SILK_TOUCH)
            ) > 0;
            boolean isShears = tool.is(Items.SHEARS);
            if (isSilkTouch || isShears) return drops;

            int fortune = EnchantmentHelper.getTagEnchantmentLevel(
                    context.getLevel().registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(Enchantments.FORTUNE),
                    tool
            );
            float chance = FORTUNE_CHANCES[Math.min(fortune, FORTUNE_CHANCES.length - 1)];

            if (context.getRandom().nextFloat() < chance) {
                drops.add(new ItemStack(CACItems.ACORN.get()));
            }
        }
        return drops;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
