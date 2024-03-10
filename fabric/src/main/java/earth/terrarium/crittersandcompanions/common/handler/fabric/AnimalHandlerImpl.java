package earth.terrarium.crittersandcompanions.common.handler.fabric;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;

public class AnimalHandlerImpl {
    public static boolean onAnimalTame(TamableAnimal animal, Player player) {
        //Todo make a PR to fabric
        return true;
    }

    public static Attribute getSwimSpeedAttribute() {
        return AdditionalEntityAttributes.WATER_SPEED;
    }
}
