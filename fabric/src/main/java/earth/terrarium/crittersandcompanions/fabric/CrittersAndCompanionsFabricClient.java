package earth.terrarium.crittersandcompanions.fabric;

import earth.terrarium.crittersandcompanions.client.CrittersAndCompanionsClient;
import earth.terrarium.crittersandcompanions.common.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class CrittersAndCompanionsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemProperties.register(ModItems.DUMBO_OCTOPUS_BUCKET.get(), new ResourceLocation("variant"), (stack, clientLevel, entity, seed) -> {
            if (stack.getTag() != null && stack.getTag().contains("BucketVariant")) {
                return stack.getTag().getInt("BucketVariant");
            } else {
                return 0.0F;
            }
        });

        ItemProperties.register(ModItems.SEA_BUNNY_BUCKET.get(), new ResourceLocation("variant"), (stack, clientLevel, entity, seed) -> {
            if (stack.getTag() != null && stack.getTag().contains("BucketVariant")) {
                return stack.getTag().getInt("BucketVariant");
            } else {
                return 0.0F;
            }
        });

        CrittersAndCompanionsClient.registerItemProperties(ItemProperties::register);

        CrittersAndCompanionsClient.registerEntityRenderers(EntityRendererRegistry::register);

        CrittersAndCompanionsClient.registerEntityLayer((location, definition) -> EntityModelLayerRegistry.registerModelLayer(location, definition::get));
    }
}
