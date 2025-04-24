package fuzs.dyedflames.fabric.client;

import fuzs.dyedflames.DyedFlames;
import fuzs.dyedflames.client.DyedFlamesClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class DyedFlamesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(DyedFlames.MOD_ID, DyedFlamesClient::new);
    }
}
