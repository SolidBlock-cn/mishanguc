package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.RenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import pers.solid.mishang.uc.text.SpecialDrawable;

/**
 * @see RenderCommandQueue
 */
@Environment(EnvType.CLIENT)
public interface RenderCommandQueueExtension {
  default void submitSpecialDrawable$mishang(MatrixStack matrixStack, SpecialDrawable specialDrawable, int light, float x, float y) {
    throw new AssertionError();
  }
}
