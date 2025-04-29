package fuzs.dyedflames.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.dyedflames.client.handler.ColoredFireOverlayHandler;
import fuzs.dyedflames.world.level.block.FireType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderDispatcher.class)
abstract class EntityRenderDispatcherMixin {

    @ModifyVariable(method = "renderFlame", at = @At("STORE"), ordinal = 0)
    private TextureAtlasSprite renderFlame$0(TextureAtlasSprite sprite, PoseStack poseStack, MultiBufferSource bufferSource, Entity entity) {
        return ColoredFireOverlayHandler.getFireOverlaySprite(entity, FireType::texture0).orElse(sprite);
    }

    @ModifyVariable(method = "renderFlame", at = @At("STORE"), ordinal = 1)
    private TextureAtlasSprite renderFlame$1(TextureAtlasSprite sprite, PoseStack poseStack, MultiBufferSource bufferSource, Entity entity) {
        return ColoredFireOverlayHandler.getFireOverlaySprite(entity, FireType::texture1).orElse(sprite);
    }
}
