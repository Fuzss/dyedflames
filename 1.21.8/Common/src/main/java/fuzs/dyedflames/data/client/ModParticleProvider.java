package fuzs.dyedflames.data.client;

import fuzs.dyedflames.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractParticleProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModParticleProvider extends AbstractParticleProvider {

    public ModParticleProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addParticles() {
        this.add(ModRegistry.SOUL_LAVA_PARTICLE_TYPE.value());
    }
}
