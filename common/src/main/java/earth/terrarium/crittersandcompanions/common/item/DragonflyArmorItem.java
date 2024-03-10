package earth.terrarium.crittersandcompanions.common.item;

import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class DragonflyArmorItem extends Item {
    private final ResourceLocation texture;
    private final int healthBuff;

    public DragonflyArmorItem(int healthBuff, String tierName, Properties properties) {
        this(healthBuff, new ResourceLocation(CrittersAndCompanions.MODID, "textures/entity/dragonfly_armor_" + tierName + ".png"), properties);
    }

    public DragonflyArmorItem(int healthBuff, ResourceLocation tierName, Properties properties) {
        super(properties);
        this.healthBuff = healthBuff;
        this.texture = tierName;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public int getHealthBuff() {
        return this.healthBuff;
    }
}
