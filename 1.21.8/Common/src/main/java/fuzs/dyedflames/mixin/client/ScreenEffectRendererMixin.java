package fuzs.dyedflames.mixin.client;

import fuzs.dyedflames.client.handler.ColoredFireOverlayHandler;
import fuzs.dyedflames.world.level.block.FireType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ScreenEffectRenderer.class)
abstract class ScreenEffectRendererMixin {

    @ModifyVariable(method = "renderFire", at = @At("STORE"), ordinal = 0)
    private static TextureAtlasSprite renderFire(TextureAtlasSprite sprite) {
        return ColoredFireOverlayHandler.getFireOverlaySprite(Minecraft.getInstance().player, FireType::texture1)
                .orElse(sprite);
    }
}
