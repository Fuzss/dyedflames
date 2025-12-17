package fuzs.dyedflames.world.level.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.dyedflames.init.ModRegistry;
import fuzs.neoforgedatapackextensions.api.v1.DataMapRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

public record FireType(Optional<TagKey<Fluid>> fluid,
                       Identifier texture0,
                       Identifier texture1,
                       Optional<SimpleParticleType> particleType) {
    public static final Codec<FireType> CODEC = RecordCodecBuilder.create(instance -> instance.group(TagKey.codec(
                            Registries.FLUID).optionalFieldOf("fluid").forGetter(FireType::fluid),
                    Identifier.CODEC.fieldOf("texture0").forGetter(FireType::texture0),
                    Identifier.CODEC.fieldOf("texture1").forGetter(FireType::texture1),
                    BuiltInRegistries.PARTICLE_TYPE.byNameCodec().flatXmap((ParticleType<?> particleType) -> {
                        return particleType instanceof SimpleParticleType simpleParticleType ?
                                DataResult.success(simpleParticleType) : DataResult.error(() -> "Unsupported type " +
                                BuiltInRegistries.PARTICLE_TYPE.getKey(particleType));
                    }, DataResult::success).optionalFieldOf("particle").forGetter(FireType::particleType))
            .apply(instance, FireType::new));

    public static Optional<FireType> getFireType(Block block) {
        return Optional.ofNullable(DataMapRegistry.INSTANCE.getData(ModRegistry.FIRE_TYPES_DATA_MAP_TYPE,
                block.builtInRegistryHolder()));
    }
}
