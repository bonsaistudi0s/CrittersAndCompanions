package io.github.bonsaistudi0s.crittersandcompanions.forge.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class ReplaceItemModifier extends LootModifier {

    public static final Codec<ReplaceItemModifier> CODEC = RecordCodecBuilder.create(builder ->
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
                    .map(WeightedEntry.Wrapper::getData)
                    .ifPresent(it -> drops.set(index, it));
        }
        return drops;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

}
