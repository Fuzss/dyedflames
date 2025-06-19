package fuzs.dyedflames.init;

import fuzs.dyedflames.DyedFlames;
import fuzs.dyedflames.world.level.block.FireType;
import fuzs.neoforgedatapackextensions.api.v1.DataMapRegistry;
import fuzs.neoforgedatapackextensions.api.v1.DataMapToken;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.network.v4.PlayerSet;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(DyedFlames.MOD_ID);
    public static final Holder.Reference<SimpleParticleType> SOUL_LAVA_PARTICLE_TYPE = REGISTRIES.registerParticleType(
            "soul_lava");

    public static final DataMapToken<Block, FireType> FIRE_TYPES_DATA_MAP_TYPE = DataMapRegistry.INSTANCE.register(
            DyedFlames.id("fire_types"),
            Registries.BLOCK,
            FireType.CODEC,
            FireType.CODEC,
            true);

    public static final DataAttachmentType<Entity, Block> LAST_FIRE_SOURCE_ATTACHMENT_TYPE = DataAttachmentRegistry.<Block>entityBuilder()
            .persistent(BuiltInRegistries.BLOCK.byNameCodec())
            .networkSynchronized(ByteBufCodecs.registry(Registries.BLOCK), PlayerSet::nearEntity)
            .build(DyedFlames.id("last_fire_source"));
    public static final DataAttachmentType<Entity, Boolean> WAS_ON_FIRE_ATTACHMENT_TYPE = DataAttachmentRegistry.<Boolean>entityBuilder()
            .build(DyedFlames.id("was_on_fire"));

    public static void bootstrap() {
        // NO-OP
    }
}
