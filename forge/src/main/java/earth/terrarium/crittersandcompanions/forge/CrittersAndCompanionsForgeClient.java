package earth.terrarium.crittersandcompanions.forge;

import earth.terrarium.crittersandcompanions.CrittersAndCompanions;
import earth.terrarium.crittersandcompanions.client.CrittersAndCompanionsClient;
import earth.terrarium.crittersandcompanions.client.renderer.BubbleLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod.EventBusSubscriber(modid = CrittersAndCompanions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CrittersAndCompanionsForgeClient {

    @SubscribeEvent
    public void init(FMLClientSetupEvent event) {
        CrittersAndCompanionsClient.registerItemProperties(ItemProperties::register);
    }

    @SubscribeEvent
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        CrittersAndCompanionsClient.registerEntityRenderers(event::registerEntityRenderer);
    }

    @SubscribeEvent
    public void addEntityLayers(EntityRenderersEvent.AddLayers event) {
        if (FMLEnvironment.dist.isClient()) {
            for (String skinName : event.getSkins()) {
                LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> skinRenderer = event.getSkin(skinName);
                if (skinRenderer != null) {
                    skinRenderer.addLayer(new BubbleLayer(skinRenderer, event.getEntityModels()));
                }
            }
        }
    }

    @SubscribeEvent
    public void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        CrittersAndCompanionsClient.registerEntityLayer(event::registerLayerDefinition);
    }
}
