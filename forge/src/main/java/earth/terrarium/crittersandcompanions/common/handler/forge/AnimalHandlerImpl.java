package earth.terrarium.crittersandcompanions.common.handler.forge;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;

public class AnimalHandlerImpl {
    public static boolean onAnimalTame(TamableAnimal animal, Player player) {
        return ForgeEventFactory.onAnimalTame(animal, player);
    }

    public static Attribute getSwimSpeedAttribute() {
        return ForgeMod.SWIM_SPEED.get();
    }
}
