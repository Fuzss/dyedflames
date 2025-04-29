package fuzs.dyedflames.neoforge;

import fuzs.dyedflames.DyedFlames;
import fuzs.dyedflames.neoforge.data.ModDataMapProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.puzzleslib.neoforge.api.data.v2.core.NeoForgeDataProviderContext;
import net.neoforged.fml.common.Mod;

@Mod(DyedFlames.MOD_ID)
public class DyedFlamesNeoForge {

    public DyedFlamesNeoForge() {
        ModConstructor.construct(DyedFlames.MOD_ID, DyedFlames::new);
        DataProviderHelper.registerDataProviders(DyedFlames.MOD_ID, (NeoForgeDataProviderContext context) -> {
            return new ModDataMapProvider(context);
        });
    }
}
