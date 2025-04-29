package fuzs.dyedflames.client.handler;

import fuzs.dyedflames.DyedFlames;
import fuzs.dyedflames.init.ModRegistry;
import fuzs.dyedflames.world.level.block.FireType;
import fuzs.puzzleslib.api.client.renderer.v1.RenderPropertyKey;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public class ColoredFireOverlayHandler {
    private static final Function<ResourceLocation, Material> FIRE_MATERIALS = Util.memoize((ResourceLocation resourceLocation) -> {
        return new Material(TextureAtlas.LOCATION_BLOCKS, resourceLocation);
    });
    public static final RenderPropertyKey<Block> LAST_FIRE_SOURCE_RENDER_PROPERTY = new RenderPropertyKey<>(DyedFlames.id(
            "last_fire_source"));

    public static void onExtractRenderState(Entity entity, EntityRenderState renderState, float partialTick) {
        Block block = ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.getOrDefault(entity, Blocks.FIRE);
        RenderPropertyKey.setRenderProperty(renderState, LAST_FIRE_SOURCE_RENDER_PROPERTY, block);
    }

    public static Optional<TextureAtlasSprite> getFireOverlaySprite(EntityRenderState renderState, Function<FireType, ResourceLocation> textureGetter) {
        return getFireOverlaySprite(RenderPropertyKey.getRenderProperty(renderState, LAST_FIRE_SOURCE_RENDER_PROPERTY),
                textureGetter);
    }

    public static Optional<TextureAtlasSprite> getFireOverlaySprite(@Nullable Entity entity, Function<FireType, ResourceLocation> textureGetter) {
        if (entity != null) {
            return getFireOverlaySprite(ModRegistry.LAST_FIRE_SOURCE_ATTACHMENT_TYPE.getOrDefault(entity, Blocks.FIRE),
                    textureGetter);
        } else {
            return Optional.empty();
        }
    }

    static Optional<TextureAtlasSprite> getFireOverlaySprite(Block block, Function<FireType, ResourceLocation> textureGetter) {
        return FireType.getFireType(block)
                .map((FireType fireType) -> FIRE_MATERIALS.apply(textureGetter.apply(fireType)).sprite());
    }
}
