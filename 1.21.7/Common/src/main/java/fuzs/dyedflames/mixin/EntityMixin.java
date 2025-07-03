package fuzs.dyedflames.mixin;

import fuzs.dyedflames.handler.EntityInsideFireHandler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
abstract class EntityMixin {
    @Shadow
    private int remainingFireTicks;

    @Inject(method = "setRemainingFireTicks", at = @At("HEAD"))
    public void setRemainingFireTicks(int remainingFireTicks, CallbackInfo callback) {
        if (remainingFireTicks > 0 && remainingFireTicks > this.remainingFireTicks) {
            EntityInsideFireHandler.setLastFireSourceFromBlocks(Entity.class.cast(this));
        }
    }
}
