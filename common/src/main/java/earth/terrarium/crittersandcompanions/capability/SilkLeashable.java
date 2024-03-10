package earth.terrarium.crittersandcompanions.capability;

import net.minecraft.world.entity.LivingEntity;

import java.util.Set;

public interface SilkLeashable {

    Set<LivingEntity> getLeashingEntities();

    Set<LivingEntity> getLeashedByEntities();

    void sendLeashState();
}
