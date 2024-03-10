package earth.terrarium.crittersandcompanions.common.capability;

import earth.terrarium.crittersandcompanions.common.entity.GrapplingHookEntity;

public interface Grapplable {

    GrapplingHookEntity getHook();

    void setHook(GrapplingHookEntity hook);
}
