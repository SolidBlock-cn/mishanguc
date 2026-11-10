package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.TextFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.render.SpecialDrawableFeatureRenderer;

import java.util.List;

/**
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
@Mixin(TextFeatureRenderer.class)
public abstract class TextFeatureRendererMixin {
  @ModifyExpressionValue(method = "buildGroup", at = @At(value = "NEW", target = "(Lnet/minecraft/client/renderer/feature/TextFeatureRenderer;)Lnet/minecraft/client/renderer/feature/TextFeatureRenderer$GlyphRenderer;"))
  private @Coerce Font.GlyphVisitor storeGlyphVisitor(@Coerce Font.GlyphVisitor original, @Share("GlyphVisitor") LocalRef<Font.GlyphVisitor> share) {

    share.set(original);
    return original;
  }

  // 此处 order = 950 是考虑到，Iris 模组在渲染文本时，会在 RETURN 时设置 ImmediateState.isRenderingBEs = false，考虑到本模组中的特殊的文本也量需要按文本的规则进行渲染，故在 Iris 模组执行 ImmediateState.isRenderingBEs = false 之前渲染。
  @Inject(method = "buildGroup", at = @At("RETURN"), order = 950)
  private void renderExtraText(FeatureFrameContext context, List<TextFeatureRenderer.Submit> submits, CallbackInfo ci, @Share("GlyphVisitor") LocalRef<Font.GlyphVisitor> share) {
    for (SpecialDrawableFeatureRenderer.SpecialDrawableSubmit submit : SpecialDrawableFeatureRenderer.currentSpecialDrawableSubmits) {
      submit.render((TextFeatureRenderer) (Object) this, share.get());
    }
  }
}
