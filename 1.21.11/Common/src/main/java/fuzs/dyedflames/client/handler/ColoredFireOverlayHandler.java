package fuzs.dyedflames.client.handler;

import fuzs.dyedflames.DyedFlames;
import fuzs.dyedflames.init.ModRegistry;
import fuzs.dyedflames.world.level.block.FireType;
import fuzs.puzzleslib.api.client.renderer.v1.RenderStateExtraData;
import net.minecraft.util.Util;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public class ColoredFireOverlayHandler {
    private static final Function<Identifier, Material> FIRE_MATERIALS = Util.memoize((Identifier identifier) -> {
        return new Material(TextureAtlas.LOCATION_BLOCKS, identifier);
    });
    public static final ContextKey<Block> LAST_FIRE_SOURCE_RENDER_PROPERTY = new ContextKey<>(DyedFlames.id(
            "last_fire_source"));

    public static void onExtractRenderState(Entity entity, EntityRenderState renderState, float partialTick) {
        Block block = ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.getOrDefault(entity, Blocks.FIRE);
        RenderStateExtraData.set(renderState, LAST_FIRE_SOURCE_RENDER_PROPERTY, block);
    }

    public static Optional<Material> getFireOverlaySprite(EntityRenderState renderState, Function<FireType, Identifier> textureGetter) {
        return getFireOverlaySprite(RenderStateExtraData.getOrDefault(renderState,
                LAST_FIRE_SOURCE_RENDER_PROPERTY,
                Blocks.AIR), textureGetter);
    }

    public static Optional<Material> getFireOverlaySprite(@Nullable Entity entity, Function<FireType, Identifier> textureGetter) {
        if (entity != null) {
            return getFireOverlaySprite(ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.getOrDefault(entity, Blocks.FIRE),
                    textureGetter);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Material> getFireOverlaySprite(Block block, Function<FireType, Identifier> textureGetter) {
        return FireType.getFireType(block)
                .map((FireType fireType) -> FIRE_MATERIALS.apply(textureGetter.apply(fireType)));
    }
}
