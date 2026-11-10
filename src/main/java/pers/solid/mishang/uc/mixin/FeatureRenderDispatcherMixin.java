package pers.solid.mishang.uc.mixin;

import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.render.SpecialDrawableFeatureRenderer;
import pers.solid.mishang.uc.render.SubmitNodeCollectionExtension;

@Mixin(FeatureRenderDispatcher.class)
public abstract class FeatureRenderDispatcherMixin {
  @Inject(method = "prepareFrameWithContext", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeStorage;drainPhases(Ljava/util/function/Consumer;)V"))
  private static void beforePrepare(FeatureFrameContext context, SubmitNodeStorage submitNodeStorage, CallbackInfoReturnable<FeatureRenderDispatcher.PreparedFrame> cir) {
    SpecialDrawableFeatureRenderer.currentSpecialDrawableSubmits = submitNodeStorage.getSubmitsPerOrder().values().stream().flatMap(submitNodeCollection -> ((SubmitNodeCollectionExtension) submitNodeCollection).getSpecialDrawableSubmits$mishang().stream()).toList();
  }
}
