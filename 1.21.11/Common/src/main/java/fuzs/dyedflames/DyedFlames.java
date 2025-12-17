package fuzs.dyedflames;

import fuzs.dyedflames.handler.EntityInsideFireHandler;
import fuzs.dyedflames.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.resources.Identifier;
import fuzs.puzzleslib.api.event.v1.entity.EntityTickEvents;
import net.minecraft.resources.Identifier;
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
        EntityTickEvents.END.register(EntityInsideFireHandler::onEndEntityTick);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
