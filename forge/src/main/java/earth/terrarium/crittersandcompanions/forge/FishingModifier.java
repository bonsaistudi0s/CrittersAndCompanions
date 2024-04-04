package earth.terrarium.crittersandcompanions.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.recipes.ItemStackCodec;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class FishingModifier extends LootModifier {
    public static final Codec<FishingModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, FishingModifier::new));

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected FishingModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> objectArrayList, LootContext context) {
        if (context.getLevel().getBiome(BlockPos.containing(context.getParam(LootContextParams.ORIGIN))).is(Biomes.RIVER)) {
            if (context.getRandom().nextFloat() < 0.15F) {
                if(context.getRandom().nextBoolean()) {
                    objectArrayList.clear();
                    objectArrayList.add(new ItemStack(ModItems.CLAM.get()));
                } else {
                    objectArrayList.clear();
                    objectArrayList.add(new ItemStack(ModItems.KOI_FISH.get()));
                }
            }
        } else {
            if (context.getRandom().nextFloat() < 0.075F) {
                objectArrayList.clear();
                objectArrayList.add(new ItemStack(ModItems.KOI_FISH.get()));
            }
        }
        return objectArrayList;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
