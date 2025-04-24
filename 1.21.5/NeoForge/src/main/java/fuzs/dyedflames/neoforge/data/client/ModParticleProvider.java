package fuzs.dyedflames.neoforge.data.client;

import fuzs.dyedflames.init.ModRegistry;
import fuzs.puzzleslib.neoforge.api.client.data.v2.AbstractParticleProvider;
import fuzs.puzzleslib.neoforge.api.data.v2.core.NeoForgeDataProviderContext;

public class ModParticleProvider extends AbstractParticleProvider {

    public ModParticleProvider(NeoForgeDataProviderContext context) {
        super(context);
    }

    @Override
    public void addParticles() {
        this.add(ModRegistry.SOUL_LAVA_PARTICLE_TYPE.value());
    }
}
