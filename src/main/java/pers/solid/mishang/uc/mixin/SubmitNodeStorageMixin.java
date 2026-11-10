package pers.solid.mishang.uc.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.render.OrderedSubmitNodeCollectorExtension;
import pers.solid.mishang.uc.render.SpecialDrawableFeatureRenderer;
import pers.solid.mishang.uc.render.SubmitNodeCollectionExtension;

import java.util.function.Consumer;

/**
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
@Mixin(SubmitNodeStorage.class)
public abstract class SubmitNodeStorageMixin implements OrderedSubmitNodeCollectorExtension {
  @Shadow
  public abstract SubmitNodeCollection order(int order);

  @Shadow
  @Final
  private Int2ObjectAVLTreeMap<SubmitNodeCollection> submitsPerOrder;

  @Override
  public void submitSpecialDrawable$mishang(SpecialDrawableFeatureRenderer.SpecialDrawableSubmit specialDrawableSubmit) {
    ((SubmitNodeCollectionExtension) order(0)).submitSpecialDrawable$mishang(specialDrawableSubmit);
  }

  @Inject(method = "drainPhases", at = @At("TAIL"))
  private void clearSpecialWhenDrainingPhases(Consumer<FeatureRenderPhase<?>> consumer, CallbackInfo ci) {
    for (SubmitNodeCollection value : submitsPerOrder.values()) {
      ((SubmitNodeCollectionExtension) value).clearSpecialDrawableSubmits$mishang();
    }
  }
}
