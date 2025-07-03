package fuzs.dyedflames.fabric;

import fuzs.dyedflames.DyedFlames;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class DyedFlamesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(DyedFlames.MOD_ID, DyedFlames::new);
    }
}
