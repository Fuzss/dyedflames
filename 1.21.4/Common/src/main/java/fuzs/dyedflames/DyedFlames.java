package fuzs.dyedflames;

import fuzs.dyedflames.handler.EntityInsideFireHandler;
import fuzs.dyedflames.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.entity.EntityTickEvents;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DyedFlames implements ModConstructor {
    public static final String MOD_ID = "dyedflames";
    public static final String MOD_NAME = "Dyed Flames";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        EntityTickEvents.START.register(EntityInsideFireHandler::onStartEntityTick);
        EntityTickEvents.END.register(EntityInsideFireHandler::onEndEntityTick);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
