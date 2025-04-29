package fuzs.dyedflames.neoforge.data.client;

import fuzs.dyedflames.init.ModRegistry;
import fuzs.puzzleslib.neoforge.api.data.v2.client.AbstractParticleDescriptionProvider;
import fuzs.puzzleslib.neoforge.api.data.v2.core.NeoForgeDataProviderContext;

public class ModParticleProvider extends AbstractParticleDescriptionProvider {

    public ModParticleProvider(NeoForgeDataProviderContext context) {
        super(context);
    }

    @Override
    public void addParticleDescriptions() {
        this.add(ModRegistry.SOUL_LAVA_PARTICLE_TYPE.value());
    }
}
