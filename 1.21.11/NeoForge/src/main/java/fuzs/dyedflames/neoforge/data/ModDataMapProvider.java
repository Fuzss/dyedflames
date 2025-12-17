package fuzs.dyedflames.neoforge.data;

import fuzs.dyedflames.init.ModRegistry;
import fuzs.dyedflames.world.level.block.FireType;
import fuzs.neoforgedatapackextensions.neoforge.api.v1.NeoForgeDataMapToken;
import net.minecraft.resources.Identifier;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {

    public ModDataMapProvider(DataProviderContext context) {
        this(context.getPackOutput(), context.getRegistries());
    }

    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider registries) {
        Builder<FireType, Block> builder = this.builder(NeoForgeDataMapToken.unwrap(ModRegistry.FIRE_TYPES_DATA_MAP_TYPE));
        register(builder,
                Blocks.FIRE,
                Identifier.withDefaultNamespace("block/fire_0"),
                Identifier.withDefaultNamespace("block/fire_1"),
                ParticleTypes.LAVA);
        register(builder,
                Blocks.LAVA,
                Optional.of(FluidTags.LAVA),
                Identifier.withDefaultNamespace("block/fire_0"),
                Identifier.withDefaultNamespace("block/fire_1"),
                ParticleTypes.LAVA);
        register(builder,
                Blocks.SOUL_FIRE,
                Identifier.withDefaultNamespace("block/soul_fire_0"),
                Identifier.withDefaultNamespace("block/soul_fire_1"),
                ModRegistry.SOUL_LAVA_PARTICLE_TYPE.value());
    }

    static void register(Builder<FireType, Block> builder, Block block, Identifier texture0, Identifier texture1, SimpleParticleType particleType) {
        register(builder, block, Optional.empty(), texture0, texture1, particleType);
    }

    static void register(Builder<FireType, Block> builder, Block block, Optional<TagKey<Fluid>> fluids, Identifier texture0, Identifier texture1, SimpleParticleType particleType) {
        builder.add(block.builtInRegistryHolder(),
                new FireType(fluids, texture0, texture1, Optional.of(particleType)),
                false);
    }
}
