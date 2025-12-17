package fuzs.dyedflames.mixin.client;

import fuzs.dyedflames.client.handler.ColoredFireOverlayHandler;
import fuzs.dyedflames.world.level.block.FireType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.MaterialSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ScreenEffectRenderer.class)
abstract class ScreenEffectRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private MaterialSet materials;

    @ModifyVariable(method = "renderScreenEffect", at = @At("STORE"), ordinal = 0)
    public TextureAtlasSprite renderScreenEffect(TextureAtlasSprite sprite) {
        // TODO migrate this to the dedicated event once it receives proper context (MaterialSet and MultiBufferSource)
        return ColoredFireOverlayHandler.getFireOverlaySprite(this.minecraft.player, FireType::texture1)
                .map(this.materials::get)
                .orElse(sprite);
    }
}
