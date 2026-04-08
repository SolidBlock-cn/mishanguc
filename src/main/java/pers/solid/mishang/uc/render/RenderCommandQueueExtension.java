package pers.solid.mishang.uc.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import pers.solid.mishang.uc.text.SpecialDrawable;

/**
 * 用于扩展 {@link OrderedSubmitNodeCollector}，通过 {@link pers.solid.mishang.uc.mixin.RenderCommandQueueMixin RenderCommandQueueMixin} 实现。
 *
 * @see OrderedSubmitNodeCollector
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
public interface RenderCommandQueueExtension {
  /**
   * 提交此模组中的 {@link SpecialDrawable} 的渲染数据。
   */
  default void submitSpecialDrawable$mishang(PoseStack matrixStack, SpecialDrawable specialDrawable, int light, float x, float y) {
    throw new AssertionError("implemented via mixin");
  }
}
