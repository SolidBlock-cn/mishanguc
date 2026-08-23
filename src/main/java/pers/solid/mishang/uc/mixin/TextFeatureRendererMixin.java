package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.TextFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
@Mixin(TextFeatureRenderer.class)
public abstract class TextFeatureRendererMixin {
  // 此处 order = 950 是考虑到，Iris 模组在渲染文本时，会在 RETURN 时设置 ImmediateState.isRenderingBEs = false，考虑到本模组中的特殊的文本也量需要按文本的规则进行渲染，故在 Iris 模组执行 ImmediateState.isRenderingBEs = false 之前渲染。
  @Inject(method = "buildGroup", at = @At("RETURN"), order = 950)
  private void renderExtraText(FeatureFrameContext context, List<TextFeatureRenderer.Submit> submits, CallbackInfo ci) {
    // 此处的渲染会直接在 TextCommandRenderer 中进行，因为光影可能会对此处的文本进行特殊处理
    final TextFeatureRenderer self = (TextFeatureRenderer) (Object) this;
    // todo 完成实现
//    for (SpecialDrawableCommand specialDrawableCommand : ((BatchingRenderCommandQueueExtension) nodeCollection).getSpecialDrawableCommands$mishang()) {
//      specialDrawableCommand.render(self);
//    }
  }
}
