package fuzs.dyedflames.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.dyedflames.client.handler.ColoredFireOverlayHandler;
import fuzs.dyedflames.world.level.block.FireType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.AtlasManager;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FlameFeatureRenderer.class)
abstract class FlameFeatureRendererMixin {

    @ModifyVariable(method = "renderFlame", at = @At("STORE"), ordinal = 0)
    private TextureAtlasSprite renderFlame$0(TextureAtlasSprite sprite, PoseStack.Pose pose, MultiBufferSource multiBufferSource, EntityRenderState entityRenderState, Quaternionf quaternionf, AtlasManager atlasManager) {
        return ColoredFireOverlayHandler.getFireOverlaySprite(entityRenderState, FireType::texture0)
                .map(atlasManager::get)
                .orElse(sprite);
    }

    @ModifyVariable(method = "renderFlame", at = @At("STORE"), ordinal = 1)
    private TextureAtlasSprite renderFlame$1(TextureAtlasSprite sprite, PoseStack.Pose pose, MultiBufferSource multiBufferSource, EntityRenderState entityRenderState, Quaternionf quaternionf, AtlasManager atlasManager) {
        return ColoredFireOverlayHandler.getFireOverlaySprite(entityRenderState, FireType::texture1)
                .map(atlasManager::get)
                .orElse(sprite);
    }
}
