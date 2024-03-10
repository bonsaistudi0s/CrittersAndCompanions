package earth.terrarium.crittersandcompanions.common.handler;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.NotImplementedException;

public class AnimalHandler {

    @ExpectPlatform
    public static boolean onAnimalTame(TamableAnimal animal, Player player) {
        throw new NotImplementedException("AnimalHandler#onAnimalTame(TamableAnimal, Player) is not implemented");
    }

    @ExpectPlatform
    public static Attribute getSwimSpeedAttribute() {
        throw new NotImplementedException("AnimalHandler#getSwimSpeedAttribute() is not implemented");
    }
}
