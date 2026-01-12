package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.RenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import pers.solid.mishang.uc.text.SpecialDrawable;

/**
 * 用于扩展 {@link RenderCommandQueue}，通过 {@link pers.solid.mishang.uc.mixin.RenderCommandQueueMixin RenderCommandQueueMixin} 实现。
 * @see RenderCommandQueue
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
public interface RenderCommandQueueExtension {
  /**
   * 提交此模组中的 {@link SpecialDrawable} 的渲染数据。
   */
  default void submitSpecialDrawable$mishang(MatrixStack matrixStack, SpecialDrawable specialDrawable, int light, float x, float y) {
    throw new AssertionError("implemented via mixin");
  }
}
