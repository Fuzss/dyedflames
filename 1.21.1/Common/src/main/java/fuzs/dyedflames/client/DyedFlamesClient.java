package fuzs.dyedflames.client;

import fuzs.dyedflames.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ParticleProvidersContext;
import net.minecraft.client.particle.LavaParticle;

public class DyedFlamesClient implements ClientModConstructor {

    @Override
    public void onRegisterParticleProviders(ParticleProvidersContext context) {
        context.registerParticleProvider(ModRegistry.SOUL_LAVA_PARTICLE_TYPE.value(), LavaParticle.Provider::new);
    }
}
