package io.github.bonsaistudi0s.crittersandcompanions.neoforge.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import org.jetbrains.annotations.NotNull;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ReplaceItemModifier extends LootModifier {

    public static final MapCodec<ReplaceItemModifier> CODEC = RecordCodecBuilder.mapCodec(builder ->
            codecStart(builder).and(
                    builder.group(
                            WeightedRandomList.codec(WeightedEntry.Wrapper.codec(ItemStack.CODEC)).fieldOf("items").forGetter(it -> it.items),
                            Codec.INT.optionalFieldOf("index", 0).forGetter(it -> it.index)
                    )
            ).apply(builder, ReplaceItemModifier::new)
    );

    private final WeightedRandomList<WeightedEntry.Wrapper<ItemStack>> items;
    private final int index;

    protected ReplaceItemModifier(LootItemCondition[] conditions, WeightedRandomList<WeightedEntry.Wrapper<ItemStack>> items, int index) {
        super(conditions);
        this.items = items;
        this.index = index;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> drops, LootContext context) {
        if (drops.size() > index) {
            items.getRandom(context.getRandom())
                    .map(WeightedEntry.Wrapper::data)
                    .ifPresent(it -> drops.set(index, it));
        }
        return drops;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

}
