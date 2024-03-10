package earth.terrarium.crittersandcompanions.common.mixin;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootPool.class)
public interface LootPoolAccessor {

    @Invoker("<init>")
    static LootPool make(LootPoolEntryContainer[] entries, LootItemCondition[] conditions, LootItemFunction[] functions, NumberProvider rolls, NumberProvider bonusRolls, String name) {
        throw new IllegalStateException();
    }

    @Accessor
    LootPoolEntryContainer[] getEntries();

    @Accessor
    LootItemCondition[] getConditions();

    @Accessor
    LootItemFunction[] getFunctions();
}
