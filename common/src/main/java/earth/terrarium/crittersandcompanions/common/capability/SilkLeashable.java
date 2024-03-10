package earth.terrarium.crittersandcompanions.common.capability;

import net.minecraft.world.entity.LivingEntity;

import java.util.Set;

public interface SilkLeashable {

    Set<LivingEntity> getLeashingEntities();

    Set<LivingEntity> getLeashedByEntities();

    void sendLeashState();
}
